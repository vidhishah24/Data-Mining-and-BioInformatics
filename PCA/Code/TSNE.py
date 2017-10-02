import numpy as np
from sklearn.manifold import TSNE
from matplotlib import pyplot as mpl
import matplotlib.cm as cm

fname= 'pca_a.txt'
data = np.genfromtxt(fname, delimiter='\t', dtype=float)

# Remove class labels
data = data[:,:-1]

#Get labels
labels = np.genfromtxt(fname, delimiter='\t', usecols=-1, dtype=str)

tsne= TSNE(n_components=2)
data_trans= tsne.fit_transform(data)
#, init='pca', learning_rate=100

data_resc= data_trans.round(decimals=2)
#data_resc=data_embedded

#visualization
lookupTable, indexed_dataSet = np.unique(labels, return_inverse=True)
color_label=np.unique(lookupTable[indexed_dataSet])
mpl.figure(1);

#for colors in indexed_dataSet:

labels = np.array(labels);
data_resc = np.column_stack((data_resc, labels));
data_resc = np.column_stack((data_resc, indexed_dataSet));
dataTemp = dict();

for colors in color_label:
    dataTemp[colors] = data_resc[data_resc[:,2]==colors]

colorCM = cm.rainbow(np.linspace(0, 1, len(color_label)))

for colors in color_label:
    mpl.scatter(dataTemp[colors][:,:1], dataTemp[colors][:,1:2], c=colorCM[int(np.asscalar(dataTemp[colors][:,3:4][0]))] , label=lookupTable[int(np.asscalar(dataTemp[colors][:,3:4][0]))])
mpl.legend()
mpl.title("TSNE for "+ fname)
mpl.show()