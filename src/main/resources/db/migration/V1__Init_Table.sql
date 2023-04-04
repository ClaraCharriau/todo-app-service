CREATE SEQUENCE task_seq;

CREATE TABLE Task (
    id UUID NOT NULL PRIMARY KEY,
    content text NOT NULL,
    category text NOT NULL,
    urgent boolean NOT NULL,
    done_date timestamp
);

INSERT INTO Task(id, content, category, urgent) VALUES(gen_random_uuid(),'Faire les courses', 'shopping', false);
INSERT INTO Task(id, content, category, urgent) VALUES(gen_random_uuid(),'Aller chez le dentiste', 'health', false);
INSERT INTO Task(id, content, category, urgent) VALUES(gen_random_uuid(),'Nettoyer le frigo', 'cleaning', false);
INSERT INTO Task(id, content, category, urgent) VALUES(gen_random_uuid(),'Payer les factures', 'bills', true);
INSERT INTO Task(id, content, category, urgent) VALUES(gen_random_uuid(),'Répondre aux mails', 'work', true);
INSERT INTO Task(id, content, category, urgent) VALUES(gen_random_uuid(),'Trouver idée de cadeau', 'other', false);
