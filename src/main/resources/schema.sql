CREATE TABLE IF NOT EXISTS rates (
    rateId INT PRIMARY KEY,
	ratingName VARCHAR(5)
    );

CREATE TABLE IF NOT EXISTS films (
    id INT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	release_date DATE,
	duration INT,
	rate INT,
	FOREIGN KEY (rate) REFERENCES rates (rateId) ON DELETE SET NULL
	);
	
