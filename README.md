Open folder jdbc in sync_server_test
Change value of the code: 
{
    String url= "jdbc:mySQL://127.0.0.1:3306/chat_app_tcp_test";
    String usreName="root";
    String passWord="@Santo.22";
}

And create database, and table with sql command:

CREATE TABLE user_account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    account VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roomlist (
    id INT PRIMARY KEY AUTO_INCREMENT,
    creater VARCHAR(255) NOT NULL,
    nameRoom VARCHAR(255) NOT NULL
);
