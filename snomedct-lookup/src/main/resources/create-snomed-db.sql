---
-- #%L
-- NICTA t3as UMLS CUI to SNOMED CT Lookup
-- %%
-- Copyright (C) 2014 NICTA
-- %%
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public
-- License along with this program.  If not, see
-- <http://www.gnu.org/licenses/gpl-3.0.html>.
-- 
-- Additional permission under GNU GPL version 3 section 7
-- 
-- If you modify this Program, or any covered work, by linking or combining
-- it with H2, GWT, or JavaBeans Activation Framework (JAF) (or a
-- modified version of those libraries), containing parts covered by the
-- terms of the H2 License, the GWT Terms, or the Common Development and
-- Distribution License (CDDL) version 1.0 ,the licensors of this Program
-- grant you additional permission to convey the resulting work.
-- #L%
---
create table snomed (
  id identity,
  snomedId varchar(25) not null,
  cui varchar(10) not null,
  termType varchar(15) not null,
  mrrank tinyint not null,
  description varchar(255) not null
);

create index idx_snomed_cui_description on snomed(cui,description);
create index idx_snomedId on snomed(snomedId);
