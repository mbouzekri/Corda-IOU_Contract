# Cordapp for IOU Contract
 🤝**Cordapp to create IOU Contracts between entities**
 
This project makes use of Corda to make a Cordapp that simulates an IOU Contract between two given nodes or more. <br />
The specifics of the state of the IOU Contract can be found in the IOUState file. The contract is verified and created following the format that can be found in IOUContrat file (value, lender, borrower).

# Instructions 
In order to run the application, you would need to first set your local Java Home to Java 8(version 8u171 or higher). <br />
Next, you will need to import a few external libraries. It is recommended to use IntelliJ IDEA as it imports all the needed jar files for you. <br />
Lastly, you would need to deploy the nodes and run them. In order to do that, you will need to run the following lines of code:
```
./gradlew deployNodes
./gradlew runnodes
```

You can find bellow an example of a contract between two nodes and the details of the corresponding IOU Contract


# IOU Contract Example
<img width="1108" alt="Screenshot 2023-07-22 at 12 19 53 PM" src="https://github.com/mbouzekri/Corda-IOU_Contract/assets/106405634/3f51e604-360f-42c2-b185-0e452b51bbb7">


# Details of the IOU Contract
<img width="1038" alt="Screenshot 2023-07-22 at 12 31 13 PM" src="https://github.com/mbouzekri/Corda-IOU_Contract/assets/106405634/f6c133dd-b27f-4325-bede-fef86cb12cac">
