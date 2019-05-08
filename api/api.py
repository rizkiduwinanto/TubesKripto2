from pymongo import MongoClient

import email, smtplib, ssl, Keccak

from email import encoders
from email.mime.base import MIMEBase
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

from flask import Flask, request, jsonify

import datetime
import easyimap
import json

app = Flask(__name__)

client = MongoClient('localhost', 27017)

db = client.crypto2
sent_mail = db.sent_mail
inbox = db.inbox

def hash(body):
  hex_body = body.encode('utf-8').hex()
  M = len(hex_body) * 4
  r = 1152
  c = 448
  n = 224
  myKeccak = Keccak.Keccak()
  md = myKeccak.Keccak((M, hex_body), r, c, n, True)
  
  return md

def signature(md, body, private_key):
  digital_signature = "<ds>" + ecceg_encrypt(private_key, md) + "</ds>"
  body = body + '\n\n' + digital_signature

def check_signature(body, public_key):
  # split body into message and digital signature
  message, ds = body.replace(' </ds>','').split('<ds> ')
  message = message.rstrip()
  ds = ds.rstrip()
  
  if hash(message) == ecceg_decrypt(ds):
    return true
  else
    return false

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

  signed_body = signature(body)

  message.attach(MIMEText(signed_body, "plain"))

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
                "body"       : signed_body, 
                "attachment" : filename,
                "encrypt_key": encrypt_key,
                "private_key": private_key,
                "timestamp"  : datetime.datetime.utcnow()}

  sent_mail_id = sent_mail.insert_one(sent_dict).inserted_id

  return jsonify(status=200,message='Message Sent',sent_mail_id=str(sent_mail_id))

@app.route('/inbox', methods=['GET'])
def get_inbox():
  uid = request.args.get('uid')
  email = request.args.get('email')
  password = request.args.get('password')

  mail_inbox = {}
  imapper = easyimap.connect('imap.gmail.com', email, password)
  i=0
  for mail_id in imapper.listids(limit=10):
    mail = imapper.mail(mail_id)
    inboxs = {}
    inboxs['from'] = mail.from_addr
    inboxs['to'] = mail.to
    inboxs['cc'] = mail.cc
    inboxs['title'] = mail.title
    inboxs['body'] = mail.body
    mail_inbox[i]=inboxs
    i+=1

  json_string = json.dumps(mail_inbox)
  return json_string

# @app.route('/sent_mail')
# def get_sent_mail():
#   uid = request.args.get('uid')
#   query = { "uid" : uid }
  
#   docs = []
#   for doc in sent_mail.find(query):
#     id = str(doc.pop('_id'))
#     doc['id'] = id
#     docs.append(doc)

#   return jsonify(docs)
  
# @app.route('/inbox')
# def get_inbox():
#   uid = request.args.get('uid')
#   query = { "uid" : uid }
  
#   docs = []
#   for doc in inbox.find(query):
#     id = str(doc.pop('_id'))
#     doc['id'] = id
#     docs.append(doc)

#   return jsonify(docs)

