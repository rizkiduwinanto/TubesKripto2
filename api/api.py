from pymongo import MongoClient

import email, smtplib, ssl

from email import encoders
from email.mime.base import MIMEBase
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

from flask import Flask, request, jsonify

import datetime

app = Flask(__name__)

client = MongoClient('localhost', 27017)

db = client.crypto2
sent_mail = db.sent_mail
inbox = db.inbox

@app.route('/')
def hello():
  return 'Hello, World!'

@app.route('/send',  methods = ['POST'])
def send_mail():
  uid = request.form['uid']
  subject = request.form['subject']
  body = request.form['body']
  sender_email = request.form['email_from']
  receiver_email = request.form['email_to']
  password = request.form['password']
  filename = request.form.get('attachment', None)
  encrypt_key = request.form.get('encrypt_key', None)
  private_key = request.form.get('private_key', None)

  message = MIMEMultipart()
  message["From"] = sender_email
  message["To"] = receiver_email
  message["Subject"] = subject

  message.attach(MIMEText(body, "plain"))

  if filename != None:
    with open(filename, "rb") as attachment:
        part = MIMEBase("application", "octet-stream")
        part.set_payload(attachment.read())
        
    encoders.encode_base64(part)

    part.add_header(
        "Content-Disposition",
        f"attachment; filename= {filename}",
    )
    message.attach(part)

  text = message.as_string()

  context = ssl.create_default_context()
  with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
      server.login(sender_email, password)
      server.sendmail(sender_email, receiver_email, text)

  sent_dict = { "uid"    : uid,
                "email_from" : sender_email, 
                "email_to"   : receiver_email, 
                "subject"    : subject, 
                "body"       : body, 
                "attachment" : filename,
                "encrypt_key": encrypt_key,
                "private_key": private_key,
                "timestamp"  : datetime.datetime.utcnow()}

  sent_mail_id = sent_mail.insert_one(sent_dict).inserted_id

  return jsonify(status=200,message='Message Sent',sent_mail_id=str(sent_mail_id))

@app.route('/sent_mail')
def get_sent_mail():
  uid = request.args.get('uid')
  query = { "uid" : uid }
  
  docs = []
  for doc in sent_mail.find(query):
    id = str(doc.pop('_id'))
    doc['id'] = id
    docs.append(doc)

  return jsonify(docs)
  
@app.route('/inbox')
def get_inbox():
  uid = request.args.get('uid')
  query = { "uid" : uid }
  
  docs = []
  for doc in inbox.find(query):
    id = str(doc.pop('_id'))
    doc['id'] = id
    docs.append(doc)

  return jsonify(docs)
