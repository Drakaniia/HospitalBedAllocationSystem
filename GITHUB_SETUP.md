# Setting up the Hospital Management System on GitHub

## Prerequisites
- Git installed on your system
- A GitHub account

## Steps to Initialize the Repository

1. **Open Command Prompt/Terminal** in the HospitalBedAllocationSystem directory:
   ```bash
   cd C:\Users\Qwenzy\Desktop\syste\HospitalBedAllocationSystem
   ```

2. **Initialize a Git repository**:
   ```bash
   git init
   ```

3. **Add all files to the repository**:
   ```bash
   git add .
   ```

4. **Make the initial commit**:
   ```bash
   git commit -m "Initial commit: Hospital Management System GUI"
   ```

5. **Add the GitHub remote repository**:
   ```bash
   git remote add origin https://github.com/Drakaniia/HospitalBedAllocationSystem.git
   ```

6. **Push the code to GitHub**:
   ```bash
   git branch -M main
   git push -u origin main
   ```

## Alternative: Cloning an Existing Repository

If the repository already exists on GitHub:

1. **Clone the repository to your local machine**:
   ```bash
   git clone https://github.com/Drakaniia/HospitalBedAllocationSystem.git
   ```

2. **Copy your project files** to the cloned directory, replacing the content

3. **Add, commit, and push the changes**:
   ```bash
   cd HospitalBedAllocationSystem
   git add .
   git commit -m "Add complete Hospital Management System GUI"
   git push origin main
   ```

## Verification

After pushing, you should be able to see your code at: https://github.com/Drakaniia/HospitalBedAllocationSystem

## Future Updates

To push future changes:
```bash
git add .
git commit -m "Your commit message"
git push
```

## Troubleshooting

- If you get an authentication error, you might need to set up SSH keys or use a personal access token
- If the repository already has content, you might need to force push or merge appropriately