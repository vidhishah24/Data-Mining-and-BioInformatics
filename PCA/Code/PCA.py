import numpy as np
from scipy import linalg as la
from matplotlib import pyplot as mpl
from numpy.distutils.lib2def import DATA_RE
from _operator import index
import matplotlib.cm as cm

#Read data from file
fname= 'pca_a.txt'
data = np.genfromtxt(fname, delimiter='\t')

#Get labels
labels = np.genfromtxt(fname, delimiter='\t', usecols=-1, dtype=str)

# Remove class labels
data = data[:,:-1]

#Get X' from X by sub mean
data -= data.mean(axis=0)

#Get Covariance matrix
covmax= np.dot(data.T, data) / (data.shape[0]-1)

#Get Eigen Values and Eigen Vectors
evals, evecs = la.eig(covmax)

# sort eigenvalue and eigenvectors in descending order
idsor = np.argsort(evals)[::-1]
evecs = evecs[:,idsor]
evals = evals[idsor]

#Get 2 eigen vectors with highest eigen values
evecs = evecs[:, :2]

#Get new values by linear combination
data_resc= np.dot(evecs.T, data.T).T
data_resc= data_resc.round(decimals=2)

lookupTable, indexed_dataSet = np.unique(labels, return_inverse=True)
color_label=np.unique(lookupTable[indexed_dataSet])
mpl.figure(1);

data_resc = np.array(data_resc);
labels = np.array(labels);
data_resc = np.column_stack((data_resc, labels));
data_resc = np.column_stack((data_resc, indexed_dataSet));
dataTemp = dict();

#for colors in indexed_dataSet:
for colors in color_label:
    dataTemp[colors] = data_resc[data_resc[:,2]==colors]
    
colorCM = cm.rainbow(np.linspace(0, 1, len(color_label)))

for colors in color_label:
    mpl.scatter(dataTemp[colors][:,:1], dataTemp[colors][:,1:2], c=colorCM[int(np.asscalar(dataTemp[colors][:,3:4][0]))] , label=lookupTable[int(np.asscalar(dataTemp[colors][:,3:4][0]))], alpha=0.5)

mpl.legend()
mpl.title("PCA for "+ fname)
mpl.show()