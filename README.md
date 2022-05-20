# java-filmorate
[Проект базы данных](DB.png)

Общее: 
    Мне показалось, что достаточно 4 таблиц.
    Первичные ключи - выделены жирным
    Знак ? - необязательность заполнения
    знак ключа рядом с email - уникальность значений в столбце

Таблица friendship:
    Неуверена в необходимости составного ключа для таблицы friendship, но показалось, что так правильнее..подскажи если нет, пожалуйста.
    Status в этой же таблице будет int, где 1 - подтвержденная дружба и 0 - неподтвержденная.

Таблица like:
    Еще подумала что возможно надо сделать составной ключ для like.. но тоже не уверена, я пока не совсем понимаю как это правильно должно работать


Примеры запросов:

SELECT Film.name 
FROM Film -- получение всех фильмов

SELECT User.login
FROM User -- получение всех пользователей

SELECT f.name, COUNT(l.filmId) AS likes
FROM Film f
JOIN like l ON f.filmId = l.filmId
GROUP BY f.filmId
ORDER BY likes desc
LIMIT 10 -- топ 10 популярных фильмов

SELECT u.login
FROM User u
JOIN friendship f ON u.user_id = f.friendId
WHERE f.userId = 1 -- найти всех друзей для пользователя 1
