The Code folder consists of 3 python files.
The libraries required to run the code are:
numpy, scipy, sklearn, matplotlib and _operator

1. PCA.py
To run open terminal
- type "python PCA.py" and press enter

The file consists of the PCA implementation using python libraries such as numpy, scipy and matplotlib.
To run the code for pca_a.txt file, we need to specify the file path and name as "fname"
e.g.,fname=pca_a.txt
To run pca_b.txt, change fname=pca_b.txt

Steps implemented:
 a. Read data from the file and remove the last column
	"fname= 'pca_a.txt'
	data = np.genfromtxt(fname, delimiter='\t', dtype=float)
	data = data[:,:-1]"
b. Calculate mean and subtract it from X to get X'
	data -= data.mean(axis=0)
c. Calculate Covariance Matrix
	covmax= np.dot(data.T, data) / (data.shape[0]-1)
d. Calculate eige vectors and eigen values
	evals, evecs = la.eig(covmax)
e. Sort Eigen values in descending order and similarly the eigen vectors
	idsor = np.argsort(evals)[::-1]
	evecs = evecs[:,idsor]
	evals = evals[idsor]
f. Take top 2 eigen vectors with highest eigen values
	evecs = evecs[:, :2]
g. Compute new set of values as a linear combination of evecs and X'
	data_resc= np.dot(evecs.T, data.T).T
h. Plot the obtained values using matplotlib and colour them according to their labels


2. SVD.py
To run open terminal
- type "python SVD.py" and press enter

The file consists of the TruncatedSVD package sklearn.decomposition and other libaries used are numpy, scipy, and matplotlib.
To run the code for pca_a.txt file, we need to specify the file path and name as "fname"
e.g.,fname=pca_a.txt
To run pca_b.txt, change fname=pca_b.txt

Steps implemented:
a. Read data from the file and remove the last column
	"fname= 'pca_a.txt'
	data = np.genfromtxt(fname, delimiter='\t', dtype=float)
	data = data[:,:-1]"
b. Compute reduced dimension using TruncatedSVD
	svd = TruncatedSVD(n_components=2)
	svd_trans = svd.fit_transform(data)
c. Plot the obtained values using matplotlib and colour them according to their labels

3. TSNE.py
To run open terminal
- type "python TSNE.py" and press enter

The file consists of the TSNE sklearn.manifold.TSNE and other libaries used are numpy, scipy and matplotlib.
To run the code for pca_a.txt file, we need to specify the file path and name as "fname"
e.g.,fname=pca_a.txt
To run pca_b.txt, change fname=pca_b.txt

Steps implemented:
a. Read data from the file and remove the last column
	"fname= 'pca_a.txt'
	data = np.genfromtxt(fname, delimiter='\t', dtype=float)
	data = data[:,:-1]"
b. Compute reduced dimension using TSNE
	tsne= TSNE(n_components=2)
	data_trans= tsne.fit_transform(data)
c. Plot the obtained values using matplotlib and colour them according to their labels
