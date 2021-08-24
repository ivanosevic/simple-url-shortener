from suds.client import Client

SOAP_SERVICE_URL = "http://localhost:7002/ws/EstudianteWebServices?wsdl"


class URL:

    def __init__(self, longUrl, shortUrl, createdAt, user, statistics):
        self.longUrl = longUrl
        self.shortUrl = shortUrl
        self.createdAt = createdAt
        self.user = user
        self.statistics = statistics


def show_menu():
    print("")
    print("1. Acortar URL")



def get_option():
    option = int(input("Digite una opcion: "))
    return option


def short_url(client, url):
    return client.service


def main():
    client = Client(url=SOAP_SERVICE_URL)
    running = True

    while (running):
        show_menu()
        option = get_option()
        if option == 1:
            url = input("Digite url a acortar")
            print(client.service.doShort("user",url))


main()
