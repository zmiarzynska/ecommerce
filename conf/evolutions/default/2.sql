# --- !Ups


INSERT INTO category(name)
VALUES ('Spring'),
       ('Summer'),
       ('Autumn'),
       ('Winter');

INSERT INTO item(name, price, description, image, category)
VALUES ('Espadrille', '100','Great for the spring time', 'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQ-44tqtx1FehL4QhcZUHdGTZnGU4wslYjHK_AkoHNOf-xH0ron8VmfYmcTcmhZ57odlpTbyy6Z5S0&usqp=CAc', 1),
       ('Boots', '300','For the cool times', 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcQUJRg_T6M7lfBuKBnT0LBwfQz8I9zq8gWCo8o4KTu_21OFLecV14U_rhk0FTxNsEEqhDrt5jO4gA&usqp=CAc', 4),
       ('Sandals', '120', 'Ideal for sunny days','https://www.eobuwie.com.pl/media/catalog/product/cache/small_image/300x300/5/9/5903419379352_01_ks.jpg', 2),
       ('High heels', '250', 'Ideal for sunny days','https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcR2SNWdMetGyRxVByzjgOrmQ1lzZK__8D0Bevm6LApvzMS6UMkoJiYKhWgiPOA72tRi4M8Cc8dJ1RI&usqp=CAc', 3);

# --- !Downs