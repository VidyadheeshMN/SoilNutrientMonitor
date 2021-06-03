import numpy as np
from os.path import dirname, join
import pickle
import sklearn

def load_model(modelfile):
	loaded_model = pickle.load(open(modelfile, 'rb'))
	return loaded_model
def predictCrop(n,p,k,ph,ec,s,cu,fe,mn,zn,b):
    feature_list = [n,p,k,ph,ec,s,cu,fe,mn,zn,b]
    single_pred = np.array(feature_list).reshape(1, -1)
    filename = join(dirname(__file__), "NBClassifier.pkl")
    loaded_model = load_model(filename)
    prediction = loaded_model.predict(single_pred)
    return prediction[0]
#"C:/Users/vidya/AndroidStudioProjects/SoilNutrientMonitor/app/src/main/python/NBClassifier.pkl"