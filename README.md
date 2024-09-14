Project for emulating an email server using a REST API

* The server receives a request in a body which contains
  the recipient and sender's email addresses and the body.
* The recipient's address is then checked to see if there's a matching vendor.
* Supported vendors are configured in vendors.csv.
* If a vendor is found, the task to send the email to the recipient is emulated in a separate thread.
* Example of usage: curl --location 'http://localhost:8081/emailServer/receive' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "toEmail": "your.email@gmail.com",
  "fromEmail": "my.email@world.com",
  "body": "Hi, how are you?"
  }'

