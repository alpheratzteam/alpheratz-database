# AlpheratzDatabase
- An experimental project of database

# HOW TO USE? EXAMPLES.

### CONNECT TO DATABASE

```java
DatabaseDriver.INSTANCE.connect(new DatabaseClient(new DatabaseData("127.0.0.1", 2139, new DatabaseUser("root", "none"))), new ConnectCallback() {
            @Override
            public void success(DatabaseClient databaseClient) {
                System.out.println("Connected!");
            }

            @Override
            public void error(Throwable cause) {
                System.err.println("Exception thrown!");
            }
        });
```

### INSERT DOCUMENT
```java
final Collection collection = database.getCollection("test");
collection.insert(Document.parse("{\"nickname\":\"abc123\"}"));
```

### UPDATE DOCUMENT
```java
final Collection collection = database.getCollection("test");
collection.update(new KeyData("nickname", "abc123"), Document.parse("{\"nickname\":\"cba321\"}"));
```

### READ ALL DOCUMENTS
```java
         final long startTime = System.currentTimeMillis();
         collection.findAsync().setFutureListener((documents) -> {
                    DatabaseDriver.INSTANCE.getLogger().info("Found " + documents.size() + " documents in " + (System.currentTimeMillis() - startTime) + " ms.");
		  });
```
		  
### READ DOCUMENT
```java
 collection.findAsync(new KeyData("nickname", "cba321")).setFutureListener((document) -> {
                    System.out.println("found: " + (Objects.nonNull(document) ? document.toString() : "null"));
                });```
