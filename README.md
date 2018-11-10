# DSA-chat

## Setup overview

The repository contains a JavaFX frontend and unit tests for all networking operations. The dependecies are managed by maven in the pom.xml in the root folder. 

**Bootstrap Server**
The bootstrap server is required to connect the individual peers to each other. Per default it listens on all interfaces on port 4000.

**GUI**
On the first startup of the GUI application allows the user to register a new user profile. After successfull registration the user profile including the private key gets stored on the local harddrive under $HOME/DSA-Chat/key.txt. During startup it tries to connect to the bootstrap server which is per default on the localhost at port 4000.

**Unit Tests**
The GUI application does not implement all available functionality yet, but the unit tests checks most of the functionality already.

## Project status

- [x] Registration of users
- [x] Fetch and save contact list
- [x] Search for other users
- [x] Create (group) chats
- [x] Send messages
- [x] Offline messages
- [x] Unit tests for basic operations
- [x] Bootstrap over local LAN
- [x] Poll for new messages
- [ ] Send message direct to peer instant over DHT
- [ ] Bootstrap over NAT
- [ ] Smart Contact File Notary

## Start GUI
1. Make sure all maven dependencies are installed
2. Start the bootstrap server (src/main/communication/BootstrapServer.java) over console `mvn exec:java -Dexec.mainClass="communication.BootstrapServer"` or your IDE of choice
3. Start the frontend (src/main/gui/Main.java) over console `mvn exec:java -Dexec.mainClass="gui.Main"` or your IDE of choice


## Start unit tests
1. Make sure all maven dependencies are installed
2. Select a unit test class in the src/test/java folder.
2. Run the test class with junit over console with `mvn clean test` or your IDE of choice


## Project goals
* A user can be registered by a unique username
* A user can set his onlinestatus visible to everyone
* A user can search other users by username and lookup their public key
* A user can store his own contact list with the username and public key information
* Public / Private keys changes of a user can be detected
* A user can start a new chat and invite other users to join
* A chat and it's messages are encrypted 
* A chat can contain two or more users
* A user can store a list of his chats
* A message can be stored offline for one week

## Communication API

### User

#### Register User

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Desired username | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption |
| --- | --- | --- | --- | ---  | --- | --- |
| PUT | hash([username]) | user | [pubkey] | [privkey] | [privkey] | - |

**Steps**

1. Choose [username]
2. Generate public / private key pair
3. Create new entry with locationKey = hash([username]), contentKey = "user"
	1. If action was successfull, user is registered, set online status immediately
	2. If action was unsuccessfull, username is already taken

#### Search User

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Searching username | x |

**Actions**

| Method | Location Key | Content Key | 
| --- | --- | --- |
| GET | hash([username]) | user |

**Steps**

1. Choose username to search
2. Try to fetch user with locationKey = hash([username]), contentKey = "user"
	1. If action was successfull user exists and the content is the public key of the searched user
	2. If action was unsuccessfull user does not exist

#### Set Online Status

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Username of user | x |
| [status] | Status of user | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption | TTL |
| --- | --- | --- | --- | ---  | --- | --- | --- |
| PUT | hash([username]) | onlinestatus | [status] | [privkey] | [privkey] | - | 60 seconds |

**Steps**

1. Set onlinestatus of own user

**Remarks**
> The online status can be verified by the signature, no one else can set the online status because of the protection
 
> The online status could be blocked by an other user, if he executes this action after the expire of the TTL before the actual user could set a value

> Online status can be viewed by anyone, an option could be to encrypt the status, but this would require to share some key with our contacts

#### Get Online Status

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Username of user | x |
| [pubkey] | Public Key of user | - |

**Actions**

| Method | Location Key | Content Key |
| --- | --- | --- |
| GET | hash([username]) | onlinestatus |

**Steps**

1. Try fetch the onlinestatus by locationKey = hash([username]), contentKey = "onlinestatus"
2. If one exists, check if the signature matches with the user [pubkey]

### Contact List

#### Get Contact List

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Username of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key |
| --- | --- | --- |
| GET | hash([username]) | contactlist |

**Steps**

1. Try fetch the contactlist by locationKey = hash([username]), contentKey = "contactlist"
2. Decrypt content value with [privkey]


#### Add Contact to Contact List

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Own Username  | x |
| [contact_username] | New Contact username | x |
| [contact_publickey] | New Contact public key | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption |
| --- | --- | --- | --- | ---  | --- | --- |
| GET | hash([contact_username]) | user |
| GET | hash([username]) | contactlist |
| PUT | hash([username]) | contactlist | [contactlist] | [privkey] | [privkey] | [pubkey] |

1. Search user with [contact_username] and retrive [contact_publickey]
2. Fetch if existing contact list exists else use empty list
3. Append new contact information to list
4. Encrypt list content with [pubkey]
5. Store new contactlist value with contentKey = hash([username]), locationKey = "contactlist"

#### Remove Contact from Contact List

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Own Username  | x |
| [contact_username] | Contact username to remove | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption |
| --- | --- | --- | --- | ---  | --- | --- |
| GET | hash([username]) | contactlist |
| PUT | hash([username]) | contactlist | [contactlist] | [privkey] | [privkey] | [pubkey] |

**Steps**

1. Fetch existing contact list
3. Remove contact information from list
4. Encrypt list content with [pubkey]
5. Store new contactlist value with contentKey = hash([username]), locationKey = "contactlist"

### Chat

#### Create Chat

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [chatid] | Chat id, random hash  | - |
| [name] | Chat name  | x |
| [chatpubkey] | Public key of chat  | x |
| [chatprivkey] | Public key of chat  | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption |
| --- | --- | --- | --- | ---  | --- | --- |
| PUT | [chatid] | name | [name] | [chatprivkey] | [chatprivkey] | [chatpubkey] |

**Steps**

1. Generate random chat id (hash) and choose chat name
2. Generate public / private key for chat administration and further communication encryption
3. Try to store chat with locationKey = [chatid], contentKey = "name"
4. Invite self and others

**Remarks**
> Everyone in the chat can rename the chat, anyone in the chat can invite other users. Everyone who knows the chats private key has full access to administration and can read every message.

> If an already invited user becomes untrusted, a new chat has to be created with all other users except the untrusted.

#### Send Chat Invite

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [chatid] | Chat id  | x |
| [inviteid] | Invite id, random generated hash  | - |
| [invite_username] | The user that gets the invite  | x |
| [invite_pubkey] | The public key of the invited user  | x |
| [chatprivkey] | Public key of chat  | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption | TTL |
| --- | --- | --- | --- | ---  | --- | --- | --- |
| PUT | hash([invite_username] + "-invite") | [inviteid] | [chatprivkey] + [chatid] | [chatprivkey] | [privkey] | [invite_pubkey] | 1 week |

**Steps**

1. Try to store invite with locationKey = hash([invite_username] + "-invite"), contentKey = [inviteid] with TTL to cleanup old invites on its own
2. Wait for users response

#### Accept Chat Invite

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Username of user | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption | TTL |
| --- | --- | --- | --- | ---  | --- | --- | --- |
| GET | hash([username] + "-invite") | all |
| PUT | hash([username] + "-member") | [chatid] | [chatprivkey] | [privkey] | [privkey] | [pubkey] |

**Steps**

1. Try to fetch open invites with own [username]
2. If invite is found and accepted, store new chat 
3. Send joining message to chat
4. Wait for TTL to delete invite on its own, because it is protected by the inviter

#### Decline Chat Invite

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Username of user | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption | TTL |
| --- | --- | --- | --- | ---  | --- | --- | --- |
| GET | hash([username] + "-invite") | all |

**Steps**

1. Try to fetch open invites with own [username]
2. If invite is found and declined, wait for TTL to delete invite on its own, because it is protected by the inviter

#### Send Chat Message

**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [username] | Username of user | x |
| [chatid] | Chat id | x |
| [messageid] | Message id, random hash | - |
| [message] | Message to send | x |
| [chatpubkey] | Public Key of user | x |
| [chatprivkey] | Private Key of user | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key | Content | Signature | Protection | Encryption | TTL |
| --- | --- | --- | --- | ---  | --- | --- | --- |
| PUT | hash([chatid] + "-message") | [messageid] | [message] | [privkey] | [chatprivkey] | [chatprivkey] | 1 week |

**Steps**

1. Generate random hash [messageid]
2. Try to store new message with locationKey = hash([chatid] + "-message"), contentKey = [messageid]

**Remarks**
> Messages can only be read by owners of the chat private key

> Everyone could inject messages on chats without knowing the chats private key, but it can be detected if the message is valid based on the used protection key.

> Every chat user with access to the chat private key can delete messages of other users

> The owner if the message can always be verified by the signature

#### Retrive Chat Message


**Parameter**

| Name | Description | Required |
| --- | --- | --- |
| [chatid] | Chat id | x |
| [chatpubkey] | Public Key of user | x |
| [chatprivkey] | Private Key of user | x |
| [pubkey] | Public Key of user | x |
| [privkey] | Private Key of user | x |

**Actions**

| Method | Location Key | Content Key |
| --- | --- | --- | 
| GET | hash([chatid] + "-message") | all | 

**Steps**

1. Fetch all stores values and verify the messages based on signature and protection keys, encrypt content with [chatprivkey]

