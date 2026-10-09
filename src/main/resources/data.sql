INSERT INTO categories (id,name) VALUES (1,'剣'),(2,'銃'),(3,'杖')
ON CONFLICT DO NOTHING;--登録しようとして何らかの競合が発生したらその行を無視する
--ID=1が既にある場合→「ID=1はもうあるな」→「じゃあ今回は何もしないでおこう」と機械が判断する
--まあ既にある場合は無視ってこと

INSERT INTO products (id,category_id,name,description,price,stock) VALUES 
(1,1,'炎龍の大剣','炎をまとう伝説の大剣（架空）',58000,10),
--今回はidを明示的に記入している
--「IDENTITY～」側は「次は１だ」と思っている（手入力のIDとは別物となっている）
--「IDENTITY～」は飽くまで「IDを指定しなければポスグレが自動的にIDを決める」という仕組み
(2,1,'月影のダガー','闇夜で光る軽量の短剣（架空）',12800,25),
(3,2,'ルミナ・ブラスター LB-9','光の弾を放つ光線銃（架空）',12800,30),
(4,2,'ノヴァ・スナイパー NS-100','長距離光線ライフル（架空）',89000,5),
(5,3,'星空の杖','星の魔力を宿す杖（架空）',33000,12)
ON CONFLICT DO NOTHING;

--IDを指定して入れると連番が進まないため、連番を最大値に合わせる
SELECT setval(pg_get_serial_sequence('categories','id'),(SELECT MAX(id) FROM categories));
--SELECT MAX(id)＝5となり、次の「IDENTITY～」は６と表示される
SELECT setval(pg_get_serial_sequence('products','id'),(SELECT MAX(id) FROM products));
--ポスグレではIDを明示的に記入すると「IDENTITY～」で自動挿入される連番は進まない
--ＩＤの重複を防ぐためにこの作業を挟んでいる