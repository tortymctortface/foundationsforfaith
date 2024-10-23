Things I Learned:
MongoDB: 
If a collection does not exist in Mongo, it will auto create one to match a new entity you added
@Document Reference - means "Only store, in this collection, the id's of the list of collections that I'm referencing - rather than the whole collection"

General:
Can check the status code, returned in the ResponseStatus, from the terminal using :
_curl -i http://localhost:8080/api/buildings_