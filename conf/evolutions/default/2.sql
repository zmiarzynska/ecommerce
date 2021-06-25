# --- !Ups


INSERT INTO category(name)
VALUES ('Spring'),
       ('Summer'),
       ('Autumn'),
       ('Winter');

INSERT INTO item(name,description, category, price, image)
VALUES ('Espadrille', 'Great for the spring time', 1, '100', 'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQ-44tqtx1FehL4QhcZUHdGTZnGU4wslYjHK_AkoHNOf-xH0ron8VmfYmcTcmhZ57odlpTbyy6Z5S0&usqp=CAc'),
       ('Boots', 'For the cool times',4, '300', 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcQUJRg_T6M7lfBuKBnT0LBwfQz8I9zq8gWCo8o4KTu_21OFLecV14U_rhk0FTxNsEEqhDrt5jO4gA&usqp=CAc'),
       ('Sandals', 'Ideal for sunny days',2, '120', 'https://www.eobuwie.com.pl/media/catalog/product/cache/small_image/300x300/5/9/5903419379352_01_ks.jpg'),
       ('High heels','Perfect for night out', 3, '250','https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcR2SNWdMetGyRxVByzjgOrmQ1lzZK__8D0Bevm6LApvzMS6UMkoJiYKhWgiPOA72tRi4M8Cc8dJ1RI&usqp=CAc');

# --- !Downs