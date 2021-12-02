# External Sorting

CS 5040 - Data structures and Algorithms

Project 3 - External Sorting

Assignment
For this project, you will implement an external sorting algorithm for binary data. The input data file will have a size of n (>1) blocks, where a block is 8K (8,192) bytes. Each block will contain a series of records, where each record is 16 bytes long and contains two fields. The first 8-byte field is a non-negative integer value of type long for the record ID and the second 8-byte field is the record key of type double, which will be used for sorting. Thus, each block contains 512 records (8192 bytes/16 bytes per record = 512 records per block).

Your program must sort the records in the file, in ascending order of the key values,using replacement selection, as described in Section 12.6 in OpenDSA in Canvas. Your program will sort sections of the file in a working memory that is 8 blocks long. To be precise, the heap will be 8 blocks in size; in addition you will also have a one block input buffer, a one-block output buffer and any additional working variables that you need.

To process the data, read the first 8 blocks of the input file into working memory and use replacement selection to create the longest possible run. As it is being created, the run is output to the one block output buffer. Whenever this output buffer becomes full, it is written to an output file called the run file. When the first run is complete, continue on to the next section of the input file, adding the second run to the end of the run file. When the process of creating runs is complete, the run file will contain some number of runs, each run being at least 8 blocks long, with the data sorted within each run. For convenience, you will probably want to begin each run in a new block. You will then use a multi-way merge to combine the runs into a single sorted file.

You must also use the 8 blocks of memory used for the heap in the run-building step to store working data from the runs during the merge step. Multi-way merging is done by reading the first block from each of the runs currently being merged into your working area, and merging these runs into the one block output buffer. When the output buffer fills up, it is written to another output file. Whenever one of the input blocks is exhausted, read in the next block for that particular run. This step requires random access (using seek()) to the run file, and a sequential write of the output file. Depending on the size of all records, you may need multiple passes of multiway-merging to sort the whole file. You should use the input buffer to read the data in from the run file to the working memory space.

Java's ByteBuffer class is useful for serializing and deserializing the record.

Invocation and I/O Files
The program will be invoked from the command-line as:

%> java Externalsort <record-filename>
where:

Externalsort is then name of the program. The file where you have your main() method must be called Externalsort.java
record-filename is the name of the file with the records to be sorted. At the end of your program, this record file (on disk) should be sorted. So this program does modify the input data file. Be careful to keep a copy of the original when you do your testing. You may assume that the specified record file does exist in our test cases.
In addition to sorting the data file, you must report some information about the execution of your program. You will need to report part of the sorted data file to standard output. Specifically, your program will print, to standard output, the first record from each 8192-byte block, in order, from the sorted data file. The records are to be printed 5 records to a line (showing both the key value and the id value for each record), the values separated by whitespace and formatted into columns. This program output must appear EXACTLY as described; ANY deviation from this requirement may result in a significant deduction in points. See the provided expected output example file to see how your output should be formatted.

You are also provided a standalone java program called tests.GenFile.java  Download tests.GenFile.javathat will generate input files with randomized data for you to use in your testing.

The tests.GenFile program can be invoked from the command-line as:

%> java tests.GenFile <filename> <size-in-blocks>
where:

tests.GenFile is the name of the program.
<filename> is the name of the file where the randomized test records will be stored.
<size in blocks> indicates the number of blocks of data to generate for the file.
Feel free to extend this file to create sorted versions of your test data, so that you can compare the output of your program to a known sorted output.

We also provide the following files to help you in this project: 

sampleInput16.bin Download sampleInput16.bin

sampleInput16Out.txt Download sampleInput16Out.txt

Sample data files will be provided in piazza to help you test your program. This is not the data file that will be used in grading your program. The test data provided to you will attempt to exercise various syntactic elements of the command specifications. It makes no effort to be comprehensive in terms of testing the data structures required by the program. Thus, while the test data provided should be useful, you should also do testing on your own test data to ensure that your program works correctly.

 

For this project, testing design will be quite different from what you have been familiar with in projects 1 and 2. You will test your codes by using another codes to generate different types of binary data. For example, you should generate testing data that’s already sorted (best case), reversely sorted (worst case), then well sorted data plus one extra un-sorted data, and data of size 2 blocks, 4 blocks, 8 blocks, 16 blocks, 32 blocks, and so on. Don’t simply count on our sample data to test your codes, and somehow you are limited by the data type or size for the files allowed to be uploaded to WebCAT. So if you write your test based on a large size file, please be aware that you may not be able to upload your test data file to WebCAT, but you can generate that data using your own code and then test it.  

 

It is also recommended to use Bytebuffer and its corresponding wrap function to build a connection between your data class and byte array. The original data are in bytes. It will be more efficient to use byte array for many operations. On the other hand, it is up to the code to interpret the bytes stored in the byte array. So the same byte array can be used for different data structures. That’ll be the case here. We will use the same memory space for both replacement selection and multiway merge. The reason is based on the fact that external sorting is used when your data size is too large to fit into memory. So we do have a firm limitation on the total number of data you are allowed to process in memory. It is also required that you use just two files for your external sorting. Please do not use an individual file for each run.
