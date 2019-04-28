import easyimap

login = input("input email address : ")
password = input("input password : ")

imapper = easyimap.connect('imap.gmail.com', login, password)
for mail_id in imapper.listids(limit=100):
    mail = imapper.mail(mail_id)
    print(mail.from_addr)
    print(mail.to)
    print(mail.cc)
    print(mail.title)
    print(mail.body)
    print(mail.attachments)