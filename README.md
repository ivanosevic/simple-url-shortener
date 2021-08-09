# Simple Url Shortener

## About the project 
Students will perform each of the requirements assigned to their respective
Projects. They must take care of the presentation of the system as well as the functionality, always
giving more priority to the latter. They can use a CSS template to have a
more finished product. The management of changes should be worked on in the control of
versions about GIT. Students must submit their changes to the same project
they are implementing and working in a collaborative way.


The system to be developed will be used to obtain a URL and return a new URL
from the project domain where the access, reference, usage graphics,
etc. See Bitly project functionality https://bitly.com/.

## Requirements
- When adding a new link, the system must present a preview of the resource. You can see the following Microlinkhq or Link preview libraries, among others.
- You want the view where all the URLs shortened by a logged-in user are displayed to work offline. Use Service Workers and browser persistence.
- Create a REST, SOAP and gRPC service that performs the following operations:
    - List of URLs published by a user including associated statistics.
    - Creation of URL record for a user returning the basic structure (full url, shortened url, creation date, statistics object, and the current image of the site (preview) in base64.
- For the REST service it is necessary to implement a security scheme based on JWT.
- Create a client on any platform that implements the operations published by the REST API.
- Create a client on any platform that implements operations published by the SOAP API.
- Create a client on any platform that implements the operations published by the gRPC API.

