import os
import smtplib
import ssl
import tkinter as tk
from tkinter import ttk
import json
import random
from email.message import EmailMessage
from datetime import date

from dotenv import load_dotenv


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


class ChefSuggestWindow:
    # Constants
    WINDOW_BG = "#E5A5ff"
    WINDOW_FONT = ("Arial", 14)
    NUM_SUGGESTIONS = 5

    def __init__(self):
        # Load meal data once.
        with open("src/meals.json", "r") as f:
            self.MEALS = json.load(f)

        # Configure Window
        self.root = tk.Tk()
        self.root.title("Chef Suggest")
        self.root.geometry("500x500")
        self.root.config(bg=ChefSuggestWindow.WINDOW_BG)

        # Generate initial suggestions
        suggestions: list[str] = get_suggestions(
            self.MEALS, ChefSuggestWindow.NUM_SUGGESTIONS
        )
        self.current_suggestions: list[tk.StringVar] = [
            tk.StringVar(value=f"{i+1}. {s}") for i, s in enumerate(suggestions)
        ]

        # Add GUI elements
        self._add_suggestions()
        self._add_buttons()

    def _add_suggestions(self) -> None:
        for suggestion in self.current_suggestions:
            label = ttk.Label(
                self.root,
                textvariable=suggestion,
                background=ChefSuggestWindow.WINDOW_BG,
                font=ChefSuggestWindow.WINDOW_FONT,
                anchor="nw",
                justify="left",
                padding=10,
            )
            label.pack(side="top", anchor="nw")

    def _add_buttons(self) -> None:
        s = ttk.Style()
        s.configure("MyFrame.TFrame", background=ChefSuggestWindow.WINDOW_BG)
        self.buttons_panel = ttk.Frame(
            self.root, width=400, height=50, style="MyFrame.TFrame"
        )

        new_suggestions_button = ttk.Button(
            self.buttons_panel,
            text="Generate New Suggestions",
            command=self.new_suggestions,
        )
        email_list_button = ttk.Button(
            self.buttons_panel,
            text="Email Suggestions to Me",
            command=self.email_suggestions,
        )

        new_suggestions_button.pack(padx=10, pady=5)
        email_list_button.pack(padx=10, pady=5)
        self.buttons_panel.pack()

    def new_suggestions(self) -> None:
        suggestions = get_suggestions(self.MEALS, self.NUM_SUGGESTIONS)
        for index, suggestion in enumerate(self.current_suggestions):
            suggestion.set(f"{index+1}. {suggestions[index]}")

    def email_suggestions(self) -> None:
        from_email = os.environ.get("CHEF_SUGGEST_EMAIL")
        password = os.environ.get("CHEF_SUGGEST_EMAIL_PASSWORD")
        to_email = os.environ.get("LIST_RECEIVER")
        msg = EmailMessage()
        today = date.today().strftime("%B %d, %Y")
        msg["Subject"] = f"Dinner Suggestions From {today}"
        msg["From"] = from_email
        msg["To"] = to_email
        suggestions: list[str] = [s.get() for s in self.current_suggestions]
        msg_content: str = ""
        for index, suggestion in enumerate(suggestions):
            msg_content += f"{index+1}. {suggestion}\n"
        msg.set_content(msg_content)

        context = ssl.create_default_context()
        with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
            server.login(from_email, password)
            server.send_message(msg)

    def start(self) -> None:
        self.root.mainloop()


def main() -> None:
    load_dotenv(".env")
    window = ChefSuggestWindow()
    window.start()


if __name__ == "__main__":
    main()
