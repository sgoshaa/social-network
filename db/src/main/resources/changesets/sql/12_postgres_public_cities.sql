INSERT INTO public.cities (name, id_country)
    (SELECT DISTINCT trim(substring(p.town, position(',' in p.town) + 1)),
        (SELECT id
            FROM public.countries
        WHERE name = trim(substring(p.town, 0, position(',' in p.town))))
     FROM public.persons p where p.town is NOT NULL  AND
             trim(substring(p.town, 0, position(',' in p.town))) <> '');