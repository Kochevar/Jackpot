Jackpot
=======

Jackpot Challenge, Celtra 2014

##Logika algoritma od 1.0 dalje
 - Deluje po principu Multi-armed Bandit, Epsilon-decreasing.
 - Privzeti E = 10%.
 - V E% potegov program izbere naključen avtomat[exploration], v (100-E)% pa izbere najbolj donosnega[exploitation].
 - Exploitation: Na začetku imajo vsi avtomati 0% donos, s časom pa ta raste glede na njihovo uspešnost.
   [1/1, 1/1, 1,1] =I=> [1/2, 1/1, 1,1] =II=> [1/2, 2/2, 1,1] =II=> [1/2, 2/3, 1,1] =III=> ...
 - V1: E se manjša s številom potegov, program postaja vse bolj pohlepen, ne pridev v poštev pri avtomatih s spremenljivo verjetnostjo.
