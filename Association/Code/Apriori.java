import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Apriori {
	static int no = 1;
	static HashMap<String, ArrayList<Integer>> UpMap = new HashMap<String, ArrayList<Integer>>();
	static HashMap<ArrayList<String>, Integer> confMap = new HashMap<ArrayList<String>, Integer>();
	static ArrayList<String> diseaseList;
	static int maxLength=0;
	public static ArrayList<ArrayList<String>> length1Itemset(ArrayList<ArrayList<String>> data, int support) {
		ArrayList<ArrayList<String>> freqCount = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < data.size(); i++) {
			int k = 0;
			for (String geneList : data.get(i)) {
				k++;
				ArrayList<Integer> sequenceList = new ArrayList<Integer>();
				if (UpMap.containsKey("G" + "" + k) && geneList.equalsIgnoreCase("Up")) {
					sequenceList = UpMap.get("G" + "" + k);
					sequenceList.add(1);

				} else if (UpMap.containsKey("G" + "" + k) && geneList.equalsIgnoreCase("Down")) {
					sequenceList = UpMap.get("G" + "" + k);
					sequenceList.add(0);

				} else if (!UpMap.containsKey("G" + "" + k) && geneList.equalsIgnoreCase("Up")) {
					sequenceList = new ArrayList<Integer>();
					sequenceList.add(1);

				} else if (!UpMap.containsKey("G" + "" + k) && geneList.equalsIgnoreCase("Down")) {
					sequenceList = new ArrayList<Integer>();
					sequenceList.add(0);

				} else if (k == data.get(i).size()) {
					if (UpMap.containsKey("G" + "" + k)) {
						sequenceList = UpMap.get("G" + "" + k);
						sequenceList.add(diseaseList.indexOf(geneList));
					} else {
						sequenceList = new ArrayList<Integer>();
						sequenceList.add(diseaseList.indexOf(geneList));
					}

				}
				UpMap.put("G" + "" + k, sequenceList);
			}
		}
		HashMap<String,Integer> diseaseCount = new HashMap<>();
		for (int i = 0; i < maxLength; i++) {
			int k = i + 1;
			ArrayList<Integer> tempListUp = UpMap.get("G" + "" + k);
			int countUp = 0;
			int countDown = 0;
			if(k!=data.get(0).size()){
			for (int j = 0; j < tempListUp.size(); j++) {
				if (tempListUp.get(j) == 1) {
					countUp++;
				}
				if (tempListUp.get(j) == 0) {
					countDown++;
				}
			}

			if (countUp >= support) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add("G" + "" + k + "_Up");
				freqCount.add(temp);
				confMap.put(new ArrayList<String>(Arrays.asList("G" + "" + k + "_Up")), countUp);
			}

			if (countDown >= support) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add("G" + "" + k + "_Down");
				freqCount.add(temp);
				confMap.put(new ArrayList<String>(Arrays.asList("G" + "" + k + "_Down")), countDown);
			}
			}
			else{
				for (int j = 0; j < tempListUp.size(); j++){
					String diseaseName=diseaseList.get(tempListUp.get(j));
					if(diseaseCount.containsKey(diseaseName)){
						diseaseCount.put(diseaseName,diseaseCount.get(diseaseName)+1);
					}else
					 diseaseCount.put(diseaseName,1);
				}
				for(String diseaseName:diseaseCount.keySet()){
					if(diseaseCount.get(diseaseName)>=support){
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(diseaseName);
						freqCount.add(temp);
						confMap.put(new ArrayList<String>(Arrays.asList(diseaseName)),diseaseCount.get(diseaseName));
					}
				}
			}

		}
		System.out.println("number of length-1 frequent itemsets: " + freqCount.size());
		return freqCount;
	}

	public static ArrayList<ArrayList<String>> subsequentItemsets(ArrayList<ArrayList<String>> first,
			ArrayList<ArrayList<String>> data, int support) {
		no++;
		Set<ArrayList<String>> second = new HashSet<ArrayList<String>>();
		for (int i = 0; i < first.size(); i++) {
			for (int j = i + 1; j < first.size(); j++) {
				Set<String> tempSet = new HashSet<>();
				tempSet.addAll(first.get(j));
				tempSet.addAll(first.get(i));
				if (tempSet.size() == no) {
					second.add(new ArrayList<String>(tempSet));
				}
			}
		}
		HashMap<ArrayList<String>, Integer> countMap = new HashMap<ArrayList<String>, Integer>();
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> itemList : second) {
			int count = 0;

			for (int j = 0; j < data.size(); j++) {

				int x = 0;
				for (String item : itemList) {
					//if(item.startsWith("G") && j<UpMap.get(item.split("_")[0]).size() || diseaseList.contains(item) && j<UpMap.get("G" + data.get(j).size()).size() ){
					if (item.startsWith("G") && item.split("_")[1].equals("Up")
							&& UpMap.get(item.split("_")[0]).get(j) == 0) {
						break;
					} else if (item.startsWith("G") && item.split("_")[1].equals("Down")
							&& UpMap.get(item.split("_")[0]).get(j) == 1) {
						break;
					}else if(diseaseList.contains(item) && UpMap.get("G" + data.get(j).size()).get(j)!=diseaseList.indexOf(item)){	
						break;}
					x++;
					}
				//}
				if (x == itemList.size()) {
					count++;
				}
			}
			if (count >= support) {
				Collections.sort(itemList);
				if (!result.contains(itemList)) {
					countMap.put(itemList, count);
					confMap.put(itemList, count);
					result.add(itemList);
				}
			}
		}

		System.out.println("number of length-" + no + " frequent itemsets: " + result.size());
		return result;

	}

	public static ArrayList<ArrayList<String>> subsets(ArrayList<String> list) {
		ArrayList<ArrayList<String>> subsetList = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < list.size(); i++) {
			ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
			for (ArrayList<String> a : subsetList) {
				temp.add(new ArrayList<String>(a));
			}
			for (ArrayList<String> a : temp) {
				a.add(list.get(i));
			}
			ArrayList<String> single = new ArrayList<String>();
			single.add(list.get(i));
			temp.add(single);
			subsetList.addAll(temp);
		}
		return subsetList;
	}

	public static ArrayList<String> template1(String part, int i, String itemList, ArrayList<Rules> rule) {
		ArrayList<String> geneList = new ArrayList<String>();
		ArrayList<String> queryResult = new ArrayList<String>();
		for (String s : itemList.split(",")) {
			geneList.add(s);
		}

		if (part.equalsIgnoreCase("RULE")) {
			for (Rules r : rule) {
				List<String> common = new ArrayList<String>(r.rule);
				common.retainAll(geneList);
				if (common.size() == 1 && i == 1) {
					queryResult.add(r.body + "-->" + r.head);
				} else if (common.size() == 0 && i == 0) {
					queryResult.add(r.body + "-->" + r.head);
				} else if (common.size() != 0 && i == -1) {
					queryResult.add(r.body + "-->" + r.head);
				}

			}
		} else if (part.equalsIgnoreCase("BODY")) {
			for (Rules r : rule) {
				List<String> common = new ArrayList<String>(r.body);
				common.retainAll(geneList);
				if (common.size() == 1 && i == 1) {
					queryResult.add(r.body + "-->" + r.head);
				} else if (common.size() == 0 && i == 0) {
					queryResult.add(r.body + "-->" + r.head);
				} else if (common.size() != 0 && i == -1) {
					queryResult.add(r.body + "-->" + r.head);
				}

			}
		} else if (part.equalsIgnoreCase("HEAD")) {
			for (Rules r : rule) {
				List<String> common = new ArrayList<String>(r.head);
				common.retainAll(geneList);
				if (common.size() == 1 && i == 1) {
					queryResult.add(r.body + "-->" + r.head);
				} else if (common.size() == 0 && i == 0) {
					queryResult.add(r.body + "-->" + r.head);
				} else if (common.size() != 0 && i == -1) {
					queryResult.add(r.body + "-->" + r.head);
				}

			}
		}

		return queryResult;

	}

	public static ArrayList<String> template2(String part, int i, ArrayList<Rules> rule) {
		ArrayList<String> queryResult = new ArrayList<String>();
		if (part.equalsIgnoreCase("RULE")) {
			for (Rules r : rule) {
				if (r.rule.size() >= i) {
					queryResult.add(r.body + "-->" + r.head);
				}
			}
		} else if (part.equalsIgnoreCase("BODY")) {
			for (Rules r : rule) {
				if (r.body.size() >= i) {
					queryResult.add(r.body + "-->" + r.head);
				}
			}
		} else if (part.equalsIgnoreCase("HEAD")) {
			for (Rules r : rule) {
				if (r.head.size() >= i) {
					queryResult.add(r.body + "-->" + r.head);
				}
			}
		}

		return queryResult;
	}

	public static ArrayList<String> template3(String s, ArrayList<Rules> rule) {
		String sArray[] = s.split(" ");
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		int length = sArray[0].length() - 1;
		if (sArray[0].charAt(0) == '1' && sArray[0].charAt(length) == '1') {
			if (sArray[2].equalsIgnoreCase("ANY")) {
				sArray[2] = "-1";
			} else if (sArray[2].equalsIgnoreCase("NONE")) {
				sArray[2] = "0";
			}
			if (sArray[5].equalsIgnoreCase("ANY")) {
				sArray[5] = "-1";
			} else if (sArray[5].equalsIgnoreCase("NONE")) {
				sArray[5] = "0";
			}
			list1 = template1(sArray[1], Integer.parseInt(sArray[2]), sArray[3], rule);
			list2 = template1(sArray[4], Integer.parseInt(sArray[5]), sArray[6], rule);

		} else if (sArray[0].charAt(0) == '2' && sArray[0].charAt(length) == '1') {
			if (sArray[4].equalsIgnoreCase("ANY")) {
				sArray[4] = "-1";
			} else if (sArray[4].equalsIgnoreCase("NONE")) {
				sArray[4] = "0";
			}
			list1 = template2(sArray[1], Integer.parseInt(sArray[2]), rule);
			list2 = template1(sArray[3], Integer.parseInt(sArray[4]), sArray[5], rule);

		} else if (sArray[0].charAt(0) == '1' && sArray[0].charAt(length) == '2') {
			if (sArray[2].equalsIgnoreCase("ANY")) {
				sArray[2] = "-1";
			} else if (sArray[2].equalsIgnoreCase("NONE")) {
				sArray[2] = "0";
			}
			list1 = template1(sArray[1], Integer.parseInt(sArray[2]), sArray[3], rule);
			list2 = template2(sArray[4], Integer.parseInt(sArray[5]), rule);

		} else if (sArray[0].charAt(0) == '2' && sArray[0].charAt(length) == '2') {
			list1 = template2(sArray[1], Integer.parseInt(sArray[2]), rule);
			list2 = template2(sArray[3], Integer.parseInt(sArray[4]), rule);
		}
		if (sArray[0].contains("AND") || sArray[0].contains("and")) {
			list1.retainAll(list2);

			return list1;
		} else {
			Set<String> resultSet = new HashSet<String>();

			resultSet.addAll(list1);
			resultSet.addAll(list2);

			return new ArrayList<String>(resultSet);
		}
	}

	public static void main(String args[]) throws IOException {

		FileReader fr = new FileReader("associationruletestdata.txt");
		BufferedReader br = new BufferedReader(fr);
		Scanner sc = new Scanner(System.in);
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		String line = "";
		Set<String> disease = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			ArrayList<String> lineList = new ArrayList<String>(Arrays.asList(line.split("\\t")));
			data.add(lineList);
			disease.add(lineList.get(lineList.size() - 1));
			if(maxLength<lineList.size())
				maxLength=lineList.size();
			
		}
		diseaseList = new ArrayList<String>(disease);
		System.out.println("Enter the support value: ");
		int support = Integer.parseInt(sc.nextLine());
		System.out.println("Enter the confidence value: ");
		int confidence = Integer.parseInt(sc.nextLine());
		long startTime = System.currentTimeMillis();
		Rules r;
		ArrayList<ArrayList<String>> length1Itemset = length1Itemset(data, support);
		ArrayList<Rules> ruleList = new ArrayList<Rules>();

		int frequentSetSize = length1Itemset.size();
		int totalLength = frequentSetSize;
		while (frequentSetSize >0) {
			length1Itemset = subsequentItemsets(length1Itemset, data, support);
			totalLength += length1Itemset.size();
			if (length1Itemset.size() != 0) {

				ArrayList<String> tempRuleList = new ArrayList<String>();

				for (int i = 0; i < length1Itemset.size(); i++) {
					ArrayList<ArrayList<String>> subsetList = new ArrayList<ArrayList<String>>();
					ArrayList<ArrayList<String>> remove = new ArrayList<ArrayList<String>>();

					subsetList.addAll(subsets(length1Itemset.get(i)));
					for (ArrayList<String> subset : subsetList) {
						if (subset.size() == length1Itemset.get(i).size()) {
							remove.add(subset);
						}
					}
					subsetList.removeAll(remove);
					for (int j = 0; j < subsetList.size(); j++) {
						for (int k = 0; k < subsetList.size(); k++) {
							ArrayList<String> tempList = new ArrayList<String>();
							tempList = new ArrayList<String>(subsetList.get(j));
							tempList.addAll(subsetList.get(k));
							Collections.sort(tempList);
							if (Collections.disjoint(subsetList.get(j), subsetList.get(k))
									&& (subsetList.get(j).size() + subsetList.get(k).size()) == length1Itemset.get(0)
											.size()
									&& !tempRuleList.contains(subsetList.get(j) + "-->" + subsetList.get(k))
									&& confMap.get(tempList) * 100 / confMap.get(subsetList.get(k)) >= confidence) {
								r = new Rules();
								r.head = subsetList.get(j);
								r.body = subsetList.get(k);
								r.rule = new ArrayList<String>(subsetList.get(j));
								r.rule.addAll(subsetList.get(k));
								ruleList.add(r);
								tempRuleList.add(subsetList.get(j) + "-->" + subsetList.get(k));
							}
						}
					}

				}

			}
			frequentSetSize = length1Itemset.size();
		}
		System.out.println("number of all lengths frequent itemsets: " + totalLength);

		System.out.println("Rules Count: " + ruleList.size());
		// for (Rules ru : ruleList) {
		// System.out.println(ru.body + "-->" + ru.head);
		// }
		System.out.println("Total time: " + (System.currentTimeMillis() - startTime) / 1000);
		System.out.println("\n" + "Enter first query: ");
		String s = sc.nextLine();
		while (!s.equals("EXIT")) {
			ArrayList<String> queryRuleList = new ArrayList<String>();
			if (s.split(" ").length == 3) {
				String sArray[] = s.split(" ");
				if (sArray[1].equalsIgnoreCase("ANY"))
					sArray[1] = "-1";
				else if (sArray[1].equalsIgnoreCase("NONE"))
					sArray[1] = "0";
				queryRuleList = template1(sArray[0], Integer.parseInt(sArray[1]), sArray[2], ruleList);
			} else if (s.split(" ").length == 2) {
				String sArray[] = s.split(" ");
				queryRuleList = template2(sArray[0], Integer.parseInt(sArray[1]), ruleList);
			} else if (s.split(" ").length > 3) {
				queryRuleList = template3(s, ruleList);
			}
			System.out.println("\n" + "Rules: " + queryRuleList);
			System.out.println("\n" + "Rule count: " + queryRuleList.size());
			// for (String a : queryRuleList) {
			// System.out.println(a);
			// }
			System.out.println("\n" + "Enter next query or EXIT: ");
			s = sc.nextLine();
		}

		br.close();
		sc.close();
	}

}

class Rules {
	ArrayList<String> head;
	ArrayList<String> body;
	ArrayList<String> rule;
}