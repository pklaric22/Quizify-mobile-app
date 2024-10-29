# Quizify

## Projektni tim

Ime i prezime | E-mail adresa (FOI) | JMBAG | Github korisničko ime | Seminarska grupa
------------  | ------------------- | ----- | --------------------- | ----------------
Andrej Pavešić |apavesic22@student.foi.hr | 0016158653 | apavesic22 | G02
Karlo Mišić | kmisic22@student.foi.hr | 0016158466 | kmisic22 | G01
Patrik Klarić | pklaric22@student.foi.hr | 0016158515 | pklaric22 | G01

## Opis domene
Quizify je aplikacija za ljubitelje kvizova, osmišljena da korisnicima pruži zabavno i izazovno iskustvo kroz raznovrsne trivije i testove znanja. Platforma pokriva širok spektar tema uključujući geografiju, književnost, povijest, glazbu, kulturu, jezike, znanost i brojne druge te omogućava korisnicima da testiraju svoje znanje, natječu se sa prijateljima ili sa globalnom zajednicom, i otkrivaju nove zanimljivosti. Quizify se ističe po tome što igrač može birati kategorije (osim ako želi "izmiješana" pitanja) na koje će odgovarati i podešavati težinu pitanja, a osim toga može dodati svoje poznanike i prijatelje kao i steći nova prijateljstva prilikom nadmetanja u znanju. Quizify prati napredak svakog igrača na način da igrač skuplja poene sa svojim točnim odgovorima koji se zapisuju u njegov osobni igrački profil. Osim što služi za zabavu Quizify pruža mogućnost stjecanja novih znanja i kvizaških iskustava, a može služiti i kao vrsta pripreme za puno ozbiljnije kvizove koji se odigravaju u stvarnom svijetu u obliku kvizaških liga i kupova. Također omogućava više načina igranja odnosno njegovi se igrači mogu natjecati u brzini davanja točnih odgovora u načinu igre koji je ograničen zadanim timerom ili se mogu natjecati u klasičnom načinu igre gdje se odgovara na zadani broj pitanja bez vremenskog ograničenja a uspješnost igrača ovisi o količini točnih odgovora.
## Specifikacija projekta

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Registracija i prijava | Quizify će korisnicima omogućiti registraciju sa svojim korisničkim podacima ukoliko im je to prvi put da koriste aplikaciju odnosno moći će se prijaviti ako su već ranije koristili aplikaciju | Andrej Pavešić
F02 | Mogućnost igranja bez prijave ili registracije | Ukoliko korisnik ne želi davati svoje osobne osobne podatke aplikacija mu omogućava da igra kao gost pri čemu mu se ne bilježe njegova postignuća i ne može uspoređivati svoje bodove sa bodovima drugih korisnika. | Karlo Mišić
F03 | Kreiranje kviza | Quizify će imati mogućnost kreiranja kviza na način da ga kreira prema kategorijama pitanja, načinu igre i težini. Moći će kreirati kviz prema nasumično odabranim kategorijama ili kategorijama koje korisnik odabere prema svojim željama i isto tako će ih moći kreirati prema težini (lako, srednje, teško). | Patrik Klarić
F04 | Praćenje uspjeha igrača | Aplikacija će imati u sebi implementiran sistem bodovanja za svaki točan ili netočan odgovor koji će korisnik dati prilikom čega se njegov napredak sprema i pohranjuje u ljestvicu uspjeha sa drugim igračima. | Andrej Pavešić
F05 | Prikaz tablice uspješnosti | Unutar aplikacije postojat će mogućnost prikaza tablice sa poretkom igrača gdje su igrači poredani prema svojem bodovnom postignuću. Pritom će postojati dvije tablice, jedna gdje će se korisnik moći uspoređivati sa ostalim korisnicima koje je označio kao svoje prijatelje, a druga gdje će biti poredani top 10 igrača sa najvećim brojem bodova. | Andrej Pavešić
F06 | Mogućnost označivanja drugih igrača kao "prijatelja" | Aplikacija mora imati mogućnost da svaki igrač može označiti prema korisničkom imenu drugog igrača kao prijatelja te se isto tako svaki zahtjev za prijateljstvom s druge strane može prihvatiti ili odbiti. | Patrik Klarić
F07 | Prikaz uspjeha po kategoriji | Korisnik će unutar aplikacije imati prikaz svojeg uspjeha po svakoj kategoriji u aplikaciji. | Karlo Mišić
F08 | Sudjelovanje u kvizu | Korisnici sudjeluju u kvizu tako što se prijave pa pokrenu novu igru i odaberu način, kategoriju i težinu pitanja. | Patrik Klarić 
F09 | Uređivanje profila | Korisnici mogu ažurirati svoje osobne podatke, profilnu sliku i druge informacije u svom korisničkom profilu. | Andrej Pavešić
F10 | Nagrade za postignuća | Aplikacija će imati sistem da se igračima dodjeljuju nagrade i priznanja za određena postignuća kao što su primjerice prvih 10 točno odgovorenih pitanja | Karlo Mišić
F11 | Kreiranje baze pitanja | Quizify će svoju bazu pitanja povlačiti iz API-ja sa linka https://opentdb.com/api.php?amount=10 te će ih koristiti u kreiranju kviza i pitanja za kviz. | Karlo Mišić

## Tehnologije i oprema
Za implementaciju rješenja koristit ćemo programski jezik Kotlin te razvojno okruženje Android Studio. Za korisničko sučelje odlučili smo se za korištenje Jetpack Compose frameworka, koji omogućuje deklarativan pristup kreiranju UI komponenata unutar Android aplikacije. Također, za verzioniranje koda koristit ćemo Git te ćemo cijeli repozitorij hostati na GitHub platformi. Dokumentaciju ćemo voditi putem GitHub Wiki sukladno uputama mentora, a zadatke projekta ćemo planirati i pratiti unutar GitHub Projects. Sve korištene tehnologije su javno dostupne i slobodne za korištenje, čime osiguravamo transparentnost i dostupnost našeg programskog proizvoda.

## Baza podataka i web server
Tražimo pristup serveru na kojemu ćemo imati bazu podataka.
