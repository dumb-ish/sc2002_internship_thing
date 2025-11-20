# Internship Placement Management System

A comprehensive Java-based system for managing internship opportunities, applications, and placements. Built with SOLID principles and clean architecture.

## Table of Contents

- [Overview](#overview)
- [System Requirements](#system-requirements)
- [Quick Start](#quick-start)
- [Compilation & Execution](#compilation--execution)
- [JavaDoc Documentation](#javadoc-documentation)

## Overview

The Internship Placement Management System facilitates the entire internship lifecycle from posting opportunities to final placements. It supports three user roles:
- **Students**: Browse and apply for internships
- **Company Representatives**: Post internships and manage applications
- **Career Center Staff**: Oversee and approve postings and representatives

## System Requirements

- **Java Development Kit (JDK)**: Version 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 512 MB RAM
- **Disk Space**: 50 MB free space

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/dumb-ish/sc2002_internship_thing.git
cd sc2002_internship_thing
```

### 2. Compile the System

**Windows (PowerShell):**
```powershell
javac -d . *.java
```

**macOS/Linux (Terminal):**
```bash
javac -d . *.java
```

### 3. Run the System

**Windows (PowerShell):**
```powershell
java InternshipPlacementSystem
```

**macOS/Linux (Terminal):**
```bash
java InternshipPlacementSystem
```

## Compilation & Execution

### Compile All Classes

To compile all Java source files in the project:

```bash
javac -d . *.java
```

**Options explained:**
- `-d .` - Places compiled `.class` files in the current directory

### Run the Application

After successful compilation, launch the system:

```bash
java InternshipPlacementSystem
```

### Compile and Run (One Command)

**Windows (PowerShell):**
```powershell
javac -d . *.java; if ($?) { java InternshipPlacementSystem }
```

**macOS/Linux (Bash):**
```bash
javac -d . *.java && java InternshipPlacementSystem
```

## JavaDoc Documentation

### Generate JavaDoc

To generate comprehensive HTML documentation:

**Windows (PowerShell):**
```powershell
javadoc -d docs -sourcepath . -private -author -version -use -windowtitle "Internship Placement Management System" -doctitle "Internship Placement Management System API Documentation" -header "SC2002 Group Project" *.java
```

**macOS/Linux (Terminal):**
```bash
javadoc -d docs -sourcepath . -private -author -version -use \
  -windowtitle "Internship Placement Management System" \
  -doctitle "Internship Placement Management System API Documentation" \
  -header "SC2002 Group Project" \
  *.java
```

**JavaDoc Options:**
- `-d docs` - Output directory for generated documentation
- `-private` - Include private members in documentation
- `-author` - Include @author tags
- `-version` - Include @version tags
- `-use` - Create class and package usage pages

### Access JavaDoc

After generation, open the documentation:

**Windows:**
```powershell
Start-Process docs\index.html
```

**macOS:**
```bash
open docs/index.html
```

**Linux:**
```bash
xdg-open docs/index.html
```

Or manually navigate to: `docs/index.html` in your web browser.

**Key Documentation Pages:**
- `index.html` - Main documentation homepage
- `allclasses-index.html` - Complete class listing
- `overview-tree.html` - Class hierarchy visualization
- `index-all.html` - Comprehensive alphabetical index

