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

import happybase as hbase

data = pd.read_csv("overdose.csv")

hostname = "10.0.100.11"
connection = hbase.Connection(hostname)
print "Connection to " + hostname + " established!"

