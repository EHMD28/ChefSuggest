import os
import smtplib
import socket
import ssl
import json
import random
from email.message import EmailMessage
from datetime import date
import sys
from dotenv import load_dotenv
from PyQt5.QtWidgets import (
    QApplication,
    QMainWindow,
    QWidget,
    QVBoxLayout,
    QLabel,
    QHBoxLayout,
    QPushButton,
    QInputDialog,
    QMessageBox,
    QLineEdit,
)
from PyQt5.QtGui import QFont, QIcon


def load_meals() -> list[str]:
    with open("src/meals.json", "r") as f:
        content = json.load(f)
        return content


def get_suggestions(meals: list[str], n: int) -> list[str]:
    num_meals: int = len(meals)
    suggested_meals: list[str] = []

    while len(suggested_meals) < n:
        rand_num = random.randint(0, num_meals - 1)
        chosen_meal = meals[rand_num]
        if chosen_meal not in suggested_meals:
            suggested_meals.append(chosen_meal)

    return suggested_meals


class ChefSuggestWindow(QMainWindow):
    WINDOW_BG = "#E5A5ff"
    WINDOW_FONT = QFont("Arial", 14)
    NUM_SUGGESTIONS = 7

    def __init__(self):
        with open("src/meals.json") as f:
            self.MEALS = json.load(f)

        super().__init__()
        self.setWindowTitle("Chef Suggest")
        self.setGeometry(0, 0, 800, 250)
        self.setWindowIcon(QIcon("src/ChefSuggestLogo.png"))
        self.init_ui()
        self.show()

    def init_ui(self):
        central_widget = QWidget()
        labels_layout = QVBoxLayout()
        central_widget.setLayout(labels_layout)
        self.setCentralWidget(central_widget)
        self.setStyleSheet(f"background-color: {ChefSuggestWindow.WINDOW_BG};")

        self.suggestions: list[QLabel] = []
        suggestions = get_suggestions(self.MEALS, self.NUM_SUGGESTIONS)
        for index, suggestion in enumerate(suggestions):
            label = QLabel(f"{index+1}. {suggestion}")
            label.setFont(self.WINDOW_FONT)
            self.suggestions.append(label)
            labels_layout.addWidget(label)
        labels_layout.addStretch()

        buttons_layout = QHBoxLayout()
        buttons_container = QWidget()
        buttons_container.setLayout(buttons_layout)
        buttons_container.setGeometry(0, 0, central_widget.width(), 100)

        new_suggestions_button = QPushButton("Generate New Suggestions")
        new_suggestions_button.setFont(self.WINDOW_FONT)
        new_suggestions_button.clicked.connect(self.new_suggestions)

        email_me_button = QPushButton("Email Me Suggestions")
        email_me_button.setFont(self.WINDOW_FONT)
        email_me_button.clicked.connect(self.email_list)

        buttons_layout.addWidget(new_suggestions_button)
        buttons_layout.addWidget(email_me_button)

        labels_layout.addWidget(buttons_container)

    def new_suggestions(self) -> None:
        suggestions = get_suggestions(self.MEALS, self.NUM_SUGGESTIONS)
        for index, suggestion in enumerate(suggestions):
            self.suggestions[index].setText(f"{index+1}. {suggestion}")

    def email_list(self) -> None:
        with open("src/config.json", "r+") as f:
            contents: dict[str, str] = json.load(f)
            to_email = contents["to_email"]

            receiver_email, ok = QInputDialog().getText(
                self,
                "Input Email",
                "Email Address",
                QLineEdit.EchoMode.Normal,
                to_email,
            )

            if ok:
                f.truncate(0)
                f.seek(0)
                contents["to_email"] = receiver_email
                json.dump(contents, f, indent=2)

        if ok and receiver_email:
            try:
                from_email = os.environ.get("CHEF_SUGGEST_EMAIL")
                password = os.environ.get("CHEF_SUGGEST_EMAIL_PASSWORD")
                msg = EmailMessage()
                today = date.today().strftime("%B %d, %Y")
                msg["Subject"] = f"Dinner Suggestions From {today}"
                msg["From"] = from_email
                msg["To"] = to_email
                suggestions = [s.text() for s in self.suggestions]
                msg_content: str = ""
                for suggestion in suggestions:
                    msg_content += f"{suggestion}\n"
                msg.set_content(msg_content)

                context = ssl.create_default_context()
                with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
                    server.login(from_email, password)
                    server.send_message(msg)
            except socket.gaierror:
                error_message = QMessageBox(self)
                error_message.setIcon(QMessageBox.Icon.Critical)
                error_message.setText("An Error Occured!")
                error_message.setInformativeText("Couldn't connect to mail server")
                error_message.show()
            except smtplib.SMTPException as e:
                error_message = QMessageBox(self)
                error_message.setIcon(QMessageBox.Icon.Critical)
                error_message.setText("An Error Occured!")
                error_message.setInformativeText(f"Error: {e}")
                error_message.show()

    def edit_items(self) -> None: ...


def main() -> None:
    load_dotenv(".env")
    app = QApplication(sys.argv)
    window = ChefSuggestWindow()
    window.show()
    sys.exit(app.exec_())


if __name__ == "__main__":
    main()
