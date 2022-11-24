-Main:
In clasa "Main" se vor initializa variabilele ce tin de statistica jocului, precum victoriile
celor doi jucatori, dar si meciurile jucate. De asemenea pentru fiecare joc, se obtin
deck-urile alese conform index-urilor oferit in input, se amesteca diferit in functie 
de shuffleSeed iar apoi sunt asignate celor doi participanti la joc. Va incepe prima runda, 
odata ce un nou joc va incepe, facandu-se schimbarile necesare inceperii unei noi runde 
(se trage prima carte din deck in mana, se actualizeaza mana jucatorilor). Apoi se va
interpreta fiecare actiune data ca input, apeland functia corespunzatoare ce va executa
comanda, din clasa ActionInterpretor.

-ActionInterpretor
Clasa ce contine toate functiile care executa comenzile date ca input in JSON file. Acestea
vor fi apelate din Main, pe masura ce sunt intalnite in array-ul de actiuni. De asemenea, 
se mai intalnesc si functii ajutatoare pentru functiile principale ce executa direct comenzile,
cum ar fi functia ce incepe o runda noua, functia care verifica a cui rand este sau functia
care verifica daca un jucator are vreun Tank pe masa. In functiile corespunzatoare comenzilor
ce afiseaza array-uri in output nu se vor folosi referinte deoarece daca de exemplu formatul
deck-ului unui jucator se va schimba de-a lungul meciului, afisarea nu va fi corecta daca
afisez deck-ul cand acesta ar trebui sa fie in format initial. Astfel, voi folosi copii ale
array-urilor pentru a afisa fix ce este in acestea in momentul primirii comenzii.

-CardTypeCaster
In aceasta clasa se gasesc metodele ce fac cast unei carti ce vine in input de tip "CardInput"
la obiecte de tip "Minion", "Environment" (in functie de numele cartii) sau "Hero" dupa caz, 
deoarece fiecare tip de carte are campuri diferite ce trebuie afisate, astfel nu se poate folosi 
clasa "CardInput" pana la finalul programului.

-Deck
Clasa Deck este folosita doar pentru a face un nou obiect, o copie a unui obiect de tip 
DeckInput deorece daca as folosi referinte, la cazurile in care sunt mai multe jocuri 
si se refoloseste deck-ul cu acelasi index, schimbarile din deck facute in jocul precedent 
ar afecta jocul urmator.

-DeckCard 
O clasa generala pentru orice carte, indiferent de tipul acesteia. Aceasta clasa va fi mostenita
de clasele Minion, Environment si Hero deoarece au campuri comune. Clasele Minion si Hero
mai au in ele campuri aditionale, cum ar fi health si attackDamage, pe cand cele Environment nu
au aceste campuri.

-Player
Clasa pentru fiecare jucator. Fiecare jucator are un erou, carti din deck, carti din mana,
mana, un boolean care arata daca este sau nu randul jucatorului, un contor al rundei pentru
a incrementa corect mana si un boolean care arata daca eroul este sau nu mort.

-Table
O clasa pentru tabla de joc care contine un array ce contine toate randurile in ordinea in
care acestea se afiseaza (de la index 0 la index 3) si un array pentru fiecare rand al
fiecarui jucator

-GameStatistics
O clasa pentru statisticile jocului din ultimele teste: numar de jocuri jucate, numarul de
victorii pentru fiecare jucator. Aceste variabile vor fi statice si initializate cu 0 odata
ce primul joc incepe.

-MinionCardAbilities / EnvironmentCardAbilities / HeroCardAbilities
Fiecare dintre aceste clasa are o functie care verifica numele cartii pentru a putea apela
abilitatea corecta. Fiecare clasa are cate o functie corespunzatoare fiecarei abilitati
existente in joc.
