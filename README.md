# Microserviciul de Întrebări

Serviciul de Topicuri și Întrebări servește ca un microserviciu în cadrul sistemului, gestionând operațiile legate de topicuri și întrebări. Acesta implementează funcționalități pentru crearea, actualizarea, ștergerea și interogarea atât a topicurilot cât și a întrebărilor din sistem.

## Descriere

Microserviciul de Întrebări este implementat folosind Spring Boot, contribuind la arhitectura de microservicii prin facilitarea gestionării topicurilor și a întrebărilor. Acesta oferă un layer robust pentru validarea și stocarea întrebărilor, și este integrat cu servicii de autentificare pentru securizarea accesului la date.

## Configurarea Proiectului

Microserviciul ste configurat printr-un set de proprietăți definite în fișierul application.properties care include:
  - Portul pe care rulează serviciul, în acest caz '8083'
  - Configurările pentru conexiunea la baza de date
  - Alte setări specifice aplicației

## Dockerfile

Proiectul include un Dockerfile pentru containerizarea și desfășurarea ușoară a Serviciului de Întrebări. Acesta este configurat pentru a rula pe portul '8083'.

## Rularea Serviciului de Întrebări cu Docker

Pentru a rula Serviciul de Întrebări într-un container Docker, urmează pașii simpli de mai jos pentru a construi și rula imaginea.

### Construirea Imaginii Docker
  - Deschide un terminal și navighează în directorul sursă al proiectului Serviciului de Întrebări, unde se află `Dockerfile`.
  - Rulează următoarea comandă pentru a construi imaginea Docker pentru Serviciul de Topicuri și Întrebări. Acest pas va crea o imagine Docker locală etichetată ca `questions-service`:

    `docker build -t questions-service .`

### Rularea Containerului Docker

După construirea imaginii, poți rula containerul folosind imaginea creată:

`docker run -p 8083:8083 questions-service`

Această comandă va porni un container din imaginea question-service, mapând portul 8083 al containerului pe portul 8083 al mașinii tale locale. Asta înseamnă că poți accesa microserviciul navigând la http://localhost:8083 în browserul tău. 

:bangbang: Însă acest pas nu este necesar pentru că există un `Dockerfile` în repository-ul central de unde se vor porni toate containerele. :bangbang:
