# Git Workflow

## Branching
- `main` is always stable. No direct commits.
- Branch names: `feature/<name>`, `fix/<name>`, `docs/<name>`.

## Commit messages
Format: `type: short description`
Types: feat, fix, test, docs, chore, refactor.

## Pull requests
- Every change goes through a PR into `main`.
- Squash and merge. Delete the branch after merging.

## Secrets
- Never commit API keys, passwords, or tokens.
- Use environment variables locally and GitHub Secrets in CI.