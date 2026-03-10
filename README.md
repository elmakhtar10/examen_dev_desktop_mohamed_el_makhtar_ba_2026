# Gestion Clinique - JavaFX / JPA (Examen GL3)

## Prerequis
- **Java 21** (ou >= 17)
- **Maven**
- **MySQL**

## Installation
1. Cloner le depot.
2. Ouvrir le projet dans IntelliJ / VS Code.
3. Verifier la version Java du projet (21 conseille).

## Base de donnees
Creer la base :
```sql
CREATE DATABASE clinique_db;
```

Verifier les identifiants dans :
`src/main/resources/META-INF/persistence.xml`

Par defaut :
```
jdbc:mysql://localhost:3306/clinique_db
user = root
password = (vide)
```

## Lancer l'application
Depuis le dossier du projet :
```bash
mvn clean javafx:run
```

Ou via l'IDE, lancer la classe :
```
com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.MainApp
```

## Creer un utilisateur admin (obligatoire)
Le mot de passe est **hash SHA-256**.

### 1) Generer le hash (PowerShell)
```powershell
$pwd = "admin123"
$bytes = [System.Text.Encoding]::UTF8.GetBytes($pwd)
$hash = [System.Security.Cryptography.SHA256]::Create().ComputeHash($bytes)
($hash | ForEach-Object { $_.ToString("x2") }) -join ""
```

### 2) Inserer l'admin en base
Remplacer `<HASH_ICI>` par le hash genere :
```sql
INSERT INTO Utilisateur
(nom, prenom, login, mot_de_passe_hash, role, type_utilisateur)
VALUES
('Admin', 'System', 'admin', '<HASH_ICI>', 'ADMIN', 'ADMIN');
```

Ensuite se connecter avec :
- **login** : `admin`
- **mot de passe** : celui qui a ete hashe (ex: `admin123`)

## Fonctionnalites
- Authentification par roles (ADMIN / MEDECIN / RECEPTIONNISTE)
- Gestion patients (CRUD + recherche)
- Gestion rendez-vous (conflit horaire interdit)
- Consultations
- Facturation + export PDF (OpenPDF)
- Ordonnance PDF

## Structure
- **model** : entites JPA
- **repository** : acces donnees
- **service** : logique metier
- **controller** + **views** : JavaFX