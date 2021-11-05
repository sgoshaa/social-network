INSERT INTO public.notifications (id, type_id, sent_time, entity_id, person_id, contact, delivered)
    (SELECT pc.id,
            'POST_COMMENT',
            pc.time,
            pc.id,
            author_id,
            'email:' || (SELECT u.e_mail
                         FROM public.users u
                         WHERE person_id = u.id) || ' phone:' || COALESCE((SELECT pers.phone
                                                                          FROM public.persons pers
                                                                          WHERE pers.id = person_id), ''),
            false
     FROM public.post_comment pc
              INNER JOIN public.posts p on p.id = pc.post_id);

INSERT INTO public.notifications (id, type_id, sent_time, entity_id, person_id, contact, delivered)
    (SELECT f.id,
            'FRIEND_REQUEST',
            current_timestamp,
            f.id,
            f.dst_person_id,
            'email:' || u.e_mail || ' phone:' || COALESCE(p.phone, ''),
            false
     FROM public.friendship f
              INNER JOIN public.persons p
                         on p.id = f.src_person_id
              INNER JOIN public.users u ON u.id = f.src_person_id);

INSERT INTO public.base_entity (id)
        (SELECT id FROM public.friendship);

INSERT INTO public.base_entity (id)
        (SELECT id FROM public.posts);

INSERT INTO public.base_entity (id)
        (SELECT id FROM public.post_comment);

INSERT INTO public.base_entity (id)
    (SELECT id FROM public.users);