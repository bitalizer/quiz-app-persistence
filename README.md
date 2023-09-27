# Quiz Application Data Layer

This project represents the data layer for a Quiz Application, providing functionality for managing quizzes.

## Introduction

This data layer project is part of a Quiz Application and is responsible for handling the persistence of questions, topics, and responses. It provides the following features:

## Features

- Create and save questions with topics and responses to the database.
- Update existing questions.
- Delete questions.
- Search questions by topic.

## Database Schema

The following tables are used in the database schema:

- `topics`: Stores information about topics.
- `questions`: Stores information about questions, including difficulty and content.
- `responses`: Stores information about responses to questions.
- `quizzes`: Stores information about quizzes.
- `quiz_question`: Represents the relationship between quizzes and questions.
