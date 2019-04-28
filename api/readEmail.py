import easyimap

login = 'kripto.mail20@gmail.com'
password = 'bismillah_1'

imapper = easyimap.connect('imap.gmail.com', login, password)
for mail_id in imapper.listids(limit=100):
    mail = imapper.mail(mail_id)
    print(mail.from_addr)
    print(mail.to)
    print(mail.cc)
    print(mail.title)
    print(mail.body)
    print(mail.attachments)