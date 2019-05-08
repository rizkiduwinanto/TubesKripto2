import easyimap
import json

login = input("input email address : ")
password = input("input password : ")

mail_inbox = {}
imapper = easyimap.connect('imap.gmail.com', login, password)
i=0
for mail_id in imapper.listids(limit=5):
    mail = imapper.mail(mail_id)
    inbox = {}
    inbox['from'] = mail.from_addr
    inbox['to'] = mail.to
    inbox['cc'] = mail.cc
    inbox['title'] = mail.title
    inbox['body'] = mail.body
    mail_inbox[i]=inbox
    i+=1

json_string = json.dumps(mail_inbox)
print(json_string)