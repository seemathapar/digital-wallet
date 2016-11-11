Digital Wallet coding challenge:

Design consideration:
1. Build the initial payment network based on the payment history transaction file paymo_input/batch_payment.txt. 
The friend of friends network can be represented as a graph of friend's (id as a Node) with the edges representing the transactions made with one or more friends as an adjacency list.
The graph data structure is built using the HashMap of keys and values as the the adjacency list.
The friend ids are HashMap keys that allow for faster lookup in O(1) time. The Hashmap values are stored in a Hashset. 
Both the HashMap and HashSet store distinct values and allow for faster lookup. That saves memory and has added benefit of constant time lookup.
a. Graph.java - HashMap <String, <AdjacencyList>>
b. AdjacencyList.java - HashSet<T>

The maximum time is spent building the initial network. It took 18 seconds to build the network for the large input batch_payment.txt downloaded from the dropbox url on my Windows XP Dual Core 8G RAM laptop. Once the graph is built it can 
service look ups in a fraction of a second. For smaller files(100 to 1000 records) this initial one time processing is done in a couple of seconds

2. Transaction lookup:
Feature 1 - With the right kind of data structures looking up a friend of friend was fairly easy. Looking up for a id in the hashMap and then a subsequent check if the hashset contains the friend's friend.
Feature 2 and 3 - Used the Breadth First Search algorith to traverse the network graph upto the specified level ( 2nd level for feature 2 friend of friend) and 4th level for Feature 3.

3. Transaction processing
The following is the flow in the AntiFraud.java class:
1. Validate the payment history file
2. Build the initial payment network from the paymo_input/batch_payment.txt
3. Validate the transaction/test file
4. Verify each transaction from the test file stream_payment.txt against the payment network graph based on the criteria (Feature1/Feature2/Feature3). 
Note: I would add the new trusted transaction to the payment Graph but I read in the FAQs to not reprocess the  file so I did not. 
5. Process the output nased on verification and write it to the respective files.

To keep the code modular and clean, I have seperated the methods based on the functionality i.e validateFile, verifyTransactions (based on criteria) and processOutput. This will enable running the Features individually.
However, when all the features are to be run together for the same input and test file it makes sense to save time and memory in expensive IO operations. 
The method processAllFeaturesWithSameDataSet reads the test file , process each transaction for all 3 features, collects the output and writes it to the respective outpout files.
For the sake of performance and efficiency, the code submitted for the challenge follows this tradeoff. 
Also, only when all of the output data is available, the code writes to the respective output files. A boolean Bitset keeps track of the verified or trusted transactions.



