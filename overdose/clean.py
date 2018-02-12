# client.py
# author: Ronald Macmaster
# 
# load an hbase table with overdose data.

import matplotlib as mpl
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import numpy as np
import scipy as sp

data = pd.read_csv("overdose.csv")
print data.columns

for col in data.columns:
    data[col] = data[col].astype(str).str.replace(",", ";")
    print data[col].head()

data.to_csv("clean.csv")
