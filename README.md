# Ex2 Spreadsheet Project

This repository contains the implementation of a simple spreadsheet application for the Ex2 assignment in the Intro to Computer Science course at Ariel University.

## Project Overview

The project simulates a basic spreadsheet application similar to Microsoft Excel or Google Sheets. It allows users to input and manipulate data in a grid of cells. Each cell can contain a number, text, or a formula. The application supports basic arithmetic operations and cell references within formulas.

## Key Features

- **Cell Types**: Cells can contain numbers, text, or formulas.
- **Formulas**: Supports basic arithmetic operations (`+`, `-`, `*`, `/`) and cell references (e.g., `=A1+B2`).
- **Error Handling**: Detects and handles errors such as cyclic references and invalid formulas.
- **GUI**: Provides a graphical user interface for interacting with the spreadsheet.
- **File Operations**: Allows saving and loading the spreadsheet to/from a file.

## Project Structure

### Classes

- **Ex2Utils**: Contains constants and utility functions used throughout the project.
- **SCell**: Represents a cell in the spreadsheet. It can contain a number, text, or a formula.
- **Ex2Sheet**: Represents the spreadsheet with a 2D array of `SCell` objects. It provides methods to get and set cell values, evaluate expressions, compute dependency depths, and save/load the sheet to/from a file.
- **CellEntry**: Represents a cell entry with coordinates in the form of "A1", "B2", etc.
- **Ex2GUI**: Provides the graphical user interface for the spreadsheet application.

## How to Run

1. Compile the Java files.
2. Run the `Ex2GUI` class to start the graphical user interface.

## Usage

- **Input Data**: Click on a cell to input data. You can enter numbers, text, or formulas.
- **Formulas**: Start a formula with `=` followed by arithmetic operations and cell references (e.g., `=A1+B2`).
- **Save/Load**: Use the provided methods to save the spreadsheet to a file or load it from a file.