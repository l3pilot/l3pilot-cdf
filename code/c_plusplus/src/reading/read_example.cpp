#include "read_example.hpp"

static const char * trav_addr_visited(trav_addr_t *visited, haddr_t addr)
{
    size_t u;           /* Local index variable */

                        /* Look for address */
    for (u = 0; u < visited->nused; u++)
        /* Check for address already in array */
        if (visited->objs[u].addr == addr)
            return(visited->objs[u].path);

    /* Didn't find address */
    return(NULL);
} /* end trav_addr_visited() */

static void trav_addr_add(trav_addr_t *visited, haddr_t addr, const char *path)
{
    size_t idx;         /* Index of address to use */

                        /* Allocate space if necessary */
    if (visited->nused == visited->nalloc) {
        visited->nalloc = MAX(1, visited->nalloc * 2);;
        visited->objs = (trav_addr_path_t *)realloc(visited->objs, visited->nalloc * sizeof(trav_addr_path_t));
    } /* end if */

      /* Append it */
    idx = visited->nused++;
    visited->objs[idx].addr = addr;
    visited->objs[idx].path = strdup(path);
} /* end trav_addr_add() */

static herr_t traverse_cb(hid_t loc_id, const char *path, const H5L_info_t *linfo,
    void *_udata)
{
    trav_ud_traverse_t *udata = (trav_ud_traverse_t *)_udata;     /* User data */
    char *new_name = NULL;
    const char *full_name;
    const char *already_visited = NULL; /* Whether the link/object was already visited */

                                        /* Create the full path name for the link */
    if (udata->is_absolute) {
        size_t base_len = strlen(udata->base_grp_name);
        size_t add_slash = base_len ? ((udata->base_grp_name)[base_len - 1] != '/') : 1;
        size_t new_name_len = base_len + add_slash + strlen(path) + 1;

        if (NULL == (new_name = (char*)malloc(new_name_len)))
            return(H5_ITER_ERROR);
        if (add_slash)
            snprintf(new_name, new_name_len, "%s/%s", udata->base_grp_name, path);
        else
            snprintf(new_name, new_name_len, "%s%s", udata->base_grp_name, path);
        full_name = new_name;
    } /* end if */
    else
        full_name = path;

    /* Perform the correct action for different types of links */
    if (linfo->type == H5L_TYPE_HARD) {
        H5O_info_t oinfo;

        /* Get information about the object */
        if (H5Oget_info_by_name(loc_id, path, &oinfo, H5P_DEFAULT) < 0) {
            if (new_name)
                free(new_name);
            return(H5_ITER_ERROR);
        } /* end if */

          /* If the object has multiple links, add it to the list of addresses
          *  already visited, if it isn't there already
          */
        if (oinfo.rc > 1)
            if (NULL == (already_visited = trav_addr_visited(udata->seen, oinfo.addr)))
                trav_addr_add(udata->seen, oinfo.addr, full_name);

        /* Make 'visit object' callback */
        if (udata->visitor->visit_obj)
            if ((*udata->visitor->visit_obj)(full_name, &oinfo, already_visited, udata->visitor->udata) < 0) {
                if (new_name)
                    free(new_name);
                return(H5_ITER_ERROR);
            } /* end if */
    } /* end if */
    else {
        /* Make 'visit link' callback */
        if (udata->visitor->visit_lnk)
            if ((*udata->visitor->visit_lnk)(full_name, linfo, udata->visitor->udata) < 0) {
                if (new_name)
                    free(new_name);
                return(H5_ITER_ERROR);
            } /* end if */
    } /* end else */

    if (new_name)
        free(new_name);

    return(H5_ITER_CONT);
} /* end traverse_cb() */

static int traverse(hid_t file_id, const char *grp_name, hbool_t visit_start,
    hbool_t recurse, const trav_visitor_t *visitor)
{
    H5O_info_t  oinfo;          /* Object info for starting group */

                                /* Get info for starting object */
    if (H5Oget_info_by_name(file_id, grp_name, &oinfo, H5P_DEFAULT) < 0)
        return -1;

    /* Visit the starting object */
    if (visit_start && visitor->visit_obj)
        (*visitor->visit_obj)(grp_name, &oinfo, NULL, visitor->udata);

    /* Go visiting, if the object is a group */
    if (oinfo.type == H5O_TYPE_GROUP) {
        trav_addr_t seen;           /* List of addresses seen */
        trav_ud_traverse_t udata;   /* User data for iteration callback */

                                    /* Init addresses seen */
        seen.nused = seen.nalloc = 0;
        seen.objs = NULL;

        /* Check for multiple links to top group */
        if (oinfo.rc > 1)
            trav_addr_add(&seen, oinfo.addr, grp_name);

        /* Set up user data structure */
        udata.seen = &seen;
        udata.visitor = visitor;
        udata.is_absolute = (*grp_name == '/');
        udata.base_grp_name = grp_name;

        /* Check for iteration of links vs. visiting all links recursively */
        if (recurse) {
            /* Visit all links in group, recursively */
            if (H5Lvisit_by_name(file_id, grp_name, trav_index_by, trav_index_order, traverse_cb, &udata, H5P_DEFAULT) < 0)
                return -1;
        } /* end if */
        else {
            /* Iterate over links in group */
            if (H5Literate_by_name(file_id, grp_name, trav_index_by, trav_index_order, NULL, traverse_cb, &udata, H5P_DEFAULT) < 0)
                return -1;
        } /* end else */

          /* Free visited addresses table */
        if (seen.objs) {
            size_t u;       /* Local index variable */

                            /* Free paths to objects */
            for (u = 0; u < seen.nused; u++)
                free(seen.objs[u].path);
            free(seen.objs);
        } /* end if */
    } /* end if */

    return 0;
}

int h5trav_visit(hid_t fid, const char *grp_name, hbool_t visit_start,
    hbool_t recurse, h5trav_obj_func_t visit_obj, h5trav_lnk_func_t visit_lnk,
    void *udata)
{
    trav_visitor_t visitor;             /* Visitor structure for objects */

                                        /* Init visitor structure */
    visitor.visit_obj = visit_obj;
    visitor.visit_lnk = visit_lnk;
    visitor.udata = udata;

    /* Traverse all objects in the file, visiting each object & link */
    if (traverse(fid, grp_name, visit_start, recurse, &visitor) < 0)
        return -1;

    return 0;
}

static void add_obj(table_t *table, haddr_t objno, const char *objname, hbool_t record)
{
    size_t u;

    /* See if we need to make table larger */
    if (table->nobjs == table->size) {
        table->size *= 2;
        table->objs = (struct h5_obj_t *)realloc(table->objs, table->size * sizeof(table->objs[0]));
    } /* end if */

      /* Increment number of objects in table */
    u = table->nobjs++;

    /* Set information about object */
    table->objs[u].objno = objno;
    table->objs[u].objname = strdup(objname);
    table->objs[u].recorded = record;
    table->objs[u].displayed = 0;
}

static herr_t
find_objs_cb(const char *name, const H5O_info_t *oinfo, const char *already_seen,
    void *op_data)
{
    find_objs_t *info = (find_objs_t*)op_data;
    herr_t ret_value = 0;

    switch (oinfo->type) {
    case H5O_TYPE_GROUP:
        if (NULL == already_seen)
            add_obj(info->group_table, oinfo->addr, name, 1);
        break;

    case H5O_TYPE_DATASET:
        if (NULL == already_seen) {
            hid_t dset;

            /* Add the dataset to the list of objects */
            add_obj(info->dset_table, oinfo->addr, name, 1);
        } /* end if */
        break;

    case H5O_TYPE_NAMED_DATATYPE:
    case H5O_TYPE_UNKNOWN:
    case H5O_TYPE_NTYPES:
    default:
        break;
    } /* end switch */

    return ret_value;
}

static void init_table(table_t **tbl)
{
    table_t *table = (table_t *)malloc(sizeof(table_t));

    table->size = 20;
    table->nobjs = 0;
    table->objs = (h5_obj_t *)malloc(table->size * sizeof(h5_obj_t));

    *tbl = table;
}

herr_t init_objs(hid_t fid, find_objs_t *info, table_t **group_table,
    table_t **dset_table)
{
    /* Initialize the tables */
    init_table(group_table);
    init_table(dset_table);

    /* Init the find_objs_t */
    info->fid = fid;
    info->group_table = *group_table;
    info->dset_table = *dset_table;

    /* Find all shared objects */
    return(h5trav_visit(fid, "/", 1, 1, find_objs_cb, NULL, info));
}

void free_table(table_t *table)
{
    unsigned u;         /* Local index value */

                        /* Free the names for the objects in the table */
    for (u = 0; u < table->nobjs; u++)
        if (table->objs[u].objname)
            free(table->objs[u].objname);

    free(table->objs);
}

static void table_list_free(void)
{
    size_t u;           /* Local index variable */

                        /* Iterate over tables */
    for (u = 0; u < table_list.nused; u++) {
        /* Release object id */
        if (H5Idec_ref(table_list.tables[u].oid) < 0)
            

        /* Free each table */
        free_table(table_list.tables[u].group_table);
        free(table_list.tables[u].group_table);
        free_table(table_list.tables[u].dset_table);
        free(table_list.tables[u].dset_table);
    }

    /* Free the table list */
    free(table_list.tables);
    table_list.tables = NULL;
    table_list.nalloc = table_list.nused = 0;
} /* end table_list_free() */

int main(int argc, char *argv[])
{
    m_filename << "D:\\l3pilot_cdf_filled_example_v0.6.h5";
    m_file = H5Fopen(m_filename.str().c_str(), H5F_ACC_RDWR, H5P_DEFAULT);

    init_objs(m_file, &info, &group_table, &dset_table);

    hsize_t dset_size, space_dims[1];
    hid_t obj, dset, space;
    

    for (int i = 0; i < info.dset_table->nobjs; ++i)
    {
        obj = H5Oopen(m_file, info.dset_table->objs[i].objname, H5P_DEFAULT);
        dset = H5Dopen2(m_file, info.dset_table->objs[i].objname, H5P_DEFAULT);
        space = H5Dget_space(dset);
        dset_size = H5Dget_storage_size(dset);
        H5Sget_simple_extent_dims(space, space_dims, NULL);
        std::cout << "Found dataset " << info.dset_table->objs[i].objname
            << " with a size of " << dset_size << " bytes and "
            << space_dims[0] << " timesteps" << std::endl;
        H5Sclose(space);
        H5Dclose(dset);
        H5Oclose(obj);
    }


    table_list_free();
    H5Fclose(m_file);
    return 0;
}
