pci
===

PCI 

This repo stands for PCI hit hot.

##To run it, run command:
com.pci.hithot.app.Publisher C:\\tmp\\pci\\resources\\clickRecords.xls C:\\tmp\\pci\\resources\\candidateTopics.xls C:\\tmp\\pci\\resources\\result.xls

##Exceptions:
Package should contain a content type part [M1.13]
Cause: it is excel version issue.
Solve: 
1. Open \template\result.xls
2. Copy your data into this excel file and save
3. App will be able recognize your xls file correctly.
