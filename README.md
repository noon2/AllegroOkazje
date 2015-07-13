# AllegroOkazje
Aplikacja do wyszukiwania okazji w serwisie Allegro (wykorzystując WebAPI)

Allegro WebAPI http://allegro.pl/webapi

Aktualnie Allegro usunelo wykorzystywane metody z WebAPI i jeszcze to nie zostalo poprawione.

Zadaniem aplikacji jest znalezienie przedmiotu z okrelonymi kryteriami jak:
- cena
- kategoria
- oferta Kup Teraz/Licytacja

Po okreleniu dane przedmiotu zostaja wyslane na serwer, ktory skanuje allegro pod okreslonymi kryteriami. 
W razie znalezienia nowego przedmiotu, który spelnia okreslone kryteria to zostaje wyslana wiadomosc do okreslonego 
uzytkownika za pomoca Google Cloud Messaging. 
