Il servlet per il login è a posto. Per il logout ho semplicemente messo invalidate() nel filtro, nel caso esso intercettasse una richiesta nella quale il parametro "logout" è settato. Possiamo benissimo cambiarlo senza problemi. Mancano ancora da fare:
- testare le richieste a positions
- fare client
- eventualmente scriverci qualche test

P.S.: HO CAMBIATO GLI URL, PERCHÈ ALMENO ERANO PIÙ CORTI DA SCRIVERE LMAO
