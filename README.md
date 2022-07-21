# Quake-Update
Quake Update is an Android Application that warns the user about recent earthquakes across the globe. The developer's API is provided by USGS website.

This is an app that gets data on earthquakes all across the globe. It uses networking concepts in android like JSON Parsing, HTTP Networking, 
Threads & Parellelism to fetch data from the website's servers. The data is in JSON format from which we parse the date, time, magnitude, and location of
the earthquake.
When using an API from an external source it is very important to analyze the data that we get from that source and modify our UI code accordingly. These knowledge helps
us to properly adjust our logic in the java code.It is very important to design the structure of the classes and methods. 
For example if the logic is related to how the information is displayed in the UI, make the code changes in the adapter class.
While establishing communication on network it is important to follow these steps: First to "form HTTP request" based on the information we want and from where we want to get it.
Second, to "send the request" to the server which will process it and figure out a proper response to send back. Third, to "receive the response" and make a sense of it.
And laslty "update the UI" code in order to display that information in our activity layout. HTTP Networking requires the usage of various important classes like
URL class which identifies the location of an Internet resource, and HTTPURLConnection class that helps us to send and receive data across the web using an HTTP connection.
Accessing something from the internet, parsing it, and displaying it on the screen is a big task which can take sometime to complete. This can block our UI making it 
feel like the screen is hanged or unresponsive. This makes the user experience very bad. 
In order to resolve this it is recommended that do not make the Network call on the main thread. Network call can be made on the background thread while other UI related features
can sill operate on the main thread. AsyncTask Callback methods can be used in order achieve this

# NOTE
I've manually added the apk file of the app with the name apk-debug.
