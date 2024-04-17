Spring Boot Web Mvc application that will allow to manage conferences and collecting applications for talks.

#### The application must provide the following REST API:
- adding a new conference (POST to /conferences) with name, topic, dates, and number of participants;
- getting a list of all conferences (GET to /conferences);
- changing information about the conference (PUT to /conferences/{conference_id});
- adding a talk to the conference (POST to /conferences/{conference_id}/talks) with the title, description, name of the speaker and type of talk (report, master class, Workshop);
- receiving a list of submitted talks for the conference (GET on /conferences/{conference_id}/talks);

Data about conferences and reports should be stored in a PostgreSQL and survive application restart, use Flyway for DB migration.

#### Business rules:
- conferences are unique by name; when trying to add a duplicate, a 409 HTTP status should be returned;
- all fields for the conference and talk  are required and must not be empty, number of participants > 100, if these rules are violated, 400 HTTP status should be returned;
- Conference dates must not overlap, otherwise, 400 HTTP status will be returned;
- the speaker cannot submit more than 3  talks; if you try to submit more, you will be refunded 400 HTTP status;
- talks  are unique in name; when trying to add a duplicate, 409 HTTP status should be returned;
- submission of talks is allowed no later than a month before the start of the conference, otherwise the 400 HTTP status is returned.
