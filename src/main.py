import tkinter as tk
from tkinter import ttk
import json
import random


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
                anchor="w",
                justify="left",
                padding=10,
            )
            label.pack(side="top", anchor="nw")

    def _add_buttons(self) -> None:
        new_suggestions_button = ttk.Button(
            self.root, text="Generate New Suggestions", command=self.new_suggestions
        )
        new_suggestions_button.pack()

    def new_suggestions(self) -> None:
        suggestions = get_suggestions(self.MEALS, self.NUM_SUGGESTIONS)
        for index, suggestion in enumerate(self.current_suggestions):
            suggestion.set(f"{index+1}. {suggestions[index]}")

    def start(self) -> None:
        self.root.mainloop()


def main() -> None:
    window = ChefSuggestWindow()
    window.start()


if __name__ == "__main__":
    main()
