# plagiarism-detection-using-cosine-similarity

System Features:

- Unzips the submitted project and detects source files (limited to c, c++ and/or java)
- All source files are merged into one. All other files (images, music) are removed from the project folder 
- Comments and identifiers (i.e name and/or student ID) are removed
- Source code is tokenized 

![alt text](/img/sc-token.png)

- Tokenized files are compared to one another
- Matching tokens are stored, and a cosine similarity value is generated for the two programs 

Sample Output:
```
CombineV1_1234526
OriginalV10_1234510
0.0423728813559322
7->5
16->22
16->25
16->34
16->35
16->36
16->45
.
.
.
107->82
107->83
107->93
107->96
107->102

```

