CREATE TABLE IF NOT EXISTS users
(
    email    CHAR(128) NOT NULL PRIMARY KEY,
    password TEXT      NOT NULL,
    progress INTEGER   NOT NULL
);

CREATE TABLE IF NOT EXISTS challenges
(
    id               INTEGER NOT NULL PRIMARY KEY,
    title            TEXT    NOT NULL,
    instructions     TEXT    NOT NULL,
    solutionTemplate TEXT    NULL
);

 CREATE TABLE IF NOT EXISTS attempts
 (
     email CHAR(128) NOT NULL,
     id INTEGER NOT NULL,
     attempt TEXT,
     PRIMARY KEY (email, id)
 );

INSERT IGNORE INTO challenges
values (1,
        'Average speed check',
        'Create a program that calculates the average speed, in mph, between two
speed cameras that are exactly 1 mile apart. You will be given
two times in epoch in order of each camera.'
           , '1');

INSERT IGNORE INTO challenges
values (2,
        'Code cracker',
        'You''ve forgotten your 4-digit phone password, however, you remember
which numbers it included. You will be given these numbers and you must
output every possible combination. Let''s hope your phone doesn''t keep count
of attempts...', '2');

INSERT IGNORE INTO challenges
values (3,
        'Hexadecimal converter',
        'You will be given a 8-bit hexadecimal number which you will
convert into denary. You will not be awarded zero for using libraries!', '3');

INSERT IGNORE INTO challenges
values (4,
        'Taxi fare',
        'A taxi company charges customers using this equation. Base fare +
fixed rate per mile. You will be given both of these rates and you must
calculate the fare of a given ride based of it''s mileage', '4');

INSERT IGNORE INTO challenges
values (5,
        'Caesar shift',
        'This basic encryption is where each letter in a string is replaced
by a letter some fixed number of positions down the alphabet. You will be
given the number of times to shift. Note: Avoid shifting spaces by accident!',
        '5');

INSERT IGNORE INTO challenges
values (6,
        'Dog age converter',
        'You need to
write a program that converts the age of a dog to the equivalent age of a
human.\n
Write an algorithm which: \n
- Asks for the age of the dog in years\n
- If the age is 2 or less, the human equivalent is 12 times the age\n
- If the age is more than 2, the human equivalent is 24 for the first 2\n
years, plus 6 for every additional year.', '6');

INSERT IGNORE INTO challenges
values (7,
        'HOW DO I TURN CAPS LOCK OFF?',
        'I ACCIDENTALLY TURNED IT ON YESTERDAY AND I DON''T KNOW HOW TO
TURN IT BACK OFF. YOU WILL BE GIVEN A STRING THAT YOU NEED TO MAKE LOWERCASE
SO MY FRIENDS DON''T THINK I''M SHOUTING AT THEM.',
        'class Challenge8 {
  /**
   * Description ....
   */
  public static int upperCaseConverter(String upperCaseString) {
    // YOUR CODE HERE
  }
}');

INSERT IGNORE INTO challenges
values (8,
        '######DISPLAYS######',
        'A display board has 20 character slots available. Your job is to
validate a word will fit, centre it and fill the blanks with #''s. If it
results in an odd number of #''s, put it on the left side', '8');
#
# INSERT INTO challenges
# values (9,
#            '',
#            ''
#        );
#
# INSERT INTO challenges
# values (10,
#            '',
#            ''
#        );