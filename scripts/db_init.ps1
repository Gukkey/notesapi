$env:PGPASSWORD='postgres'
psql -f create_user.sql -U postgres
$env:PGPASSWORD='notesapi'
psql -f create_db.sql -U notesapi -d postgres
psql -f create_notes_table.sql -U notesapi -d notesapi