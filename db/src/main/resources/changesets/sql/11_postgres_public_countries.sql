INSERT INTO public.countries (name)
(SELECT DISTINCT trim(substring(p.town, 0, position(',' in p.town)))
            FROM public.persons p where p.town is NOT NULL
                AND trim(substring(p.town, 0, position(',' in p.town))) <> ''
                AND NOT (trim(substring(p.town, 0, position(',' in p.town))) in
                    (SELECT name
                    FROM public.countries)));