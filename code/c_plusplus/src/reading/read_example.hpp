#include <structure_definitions.h>
#include <cstdlib>
#include <stdio.h>
#include <stdlib.h>
#include <sstream>
#include <iostream>

#undef MAX
#define MAX(a,b)    (((a)>(b)) ? (a) : (b))
#undef MIN
#define MIN(a,b)    (((a)<(b)) ? (a) : (b))


std::stringstream m_filename;
hid_t m_file;
H5O_info_t oi;

static H5_index_t trav_index_by = H5_INDEX_NAME;
static H5_iter_order_t trav_index_order = H5_ITER_INC;

/* Typedefs for visiting objects */
typedef herr_t(*h5trav_obj_func_t)(const char *path_name, const H5O_info_t *oinfo,
    const char *first_seen, void *udata);
typedef herr_t(*h5trav_lnk_func_t)(const char *path_name, const H5L_info_t *linfo,
    void *udata);

typedef struct {
    h5trav_obj_func_t visit_obj;        /* Callback for visiting objects */
    h5trav_lnk_func_t visit_lnk;        /* Callback for visiting links */
    void *udata;                /* User data to pass to callbacks */
} trav_visitor_t;

typedef struct trav_addr_path_t {
    haddr_t addr;
    char *path;
} trav_addr_path_t;

typedef struct trav_addr_t {
    size_t      nalloc;
    size_t      nused;
    trav_addr_path_t *objs;
} trav_addr_t;

typedef struct {
    trav_addr_t *seen;              /* List of addresses seen already */
    const trav_visitor_t *visitor;  /* Information for visiting each link/object */
    hbool_t is_absolute;            /* Whether the traversal has absolute paths */
    const char *base_grp_name;      /* Name of the group that serves as the base
                                    * for iteration */
} trav_ud_traverse_t;

/*struct taken from the dumper. needed in table struct*/
typedef struct h5_obj_t {
    haddr_t objno;
    char *objname;
    hbool_t displayed;          /* Flag to indicate that the object has been displayed */
    hbool_t recorded;           /* Flag for named datatypes to indicate they were found in the group hierarchy */
} h5_obj_t;

/*struct for the tables that the find_objs function uses*/
typedef struct table_t {
    size_t size;
    size_t nobjs;
    h5_obj_t *objs;
} table_t;

/* List of table structures.  There is one table structure for each file */
typedef struct table_items_t {
    unsigned long   fileno;         /* File number that these tables refer to */
    hid_t           oid;            /* ID of an object in this file, held open so fileno is consistent */
    table_t         *group_table;   /* Table of groups */
    table_t         *dset_table;    /* Table of datasets */
} table_items_t;

typedef struct h5dump_table_list_t {
    size_t                  nalloc;
    size_t                  nused;
    table_items_t    *tables;
} table_list_t;

/*this struct stores the information that is passed to the find_objs function*/
typedef struct find_objs_t {
    hid_t fid;
    table_t *group_table;
    table_t *dset_table;
} find_objs_t;

table_list_t    table_list = { 0, 0, NULL };
table_t         *group_table = NULL, *dset_table = NULL;

find_objs_t     info;

typedef herr_t(*H5O_iterate_t) (hid_t o_id, const char *name, const H5O_info_t *object_info, void *op_data);

static void add_obj(table_t *table, haddr_t objno, const char *objname, hbool_t recorded);

int h5trav_visit(hid_t file_id, const char *grp_name,
    hbool_t visit_start, hbool_t recurse, h5trav_obj_func_t visit_obj,
    h5trav_lnk_func_t visit_lnk, void *udata);

herr_t init_objs(hid_t fid, find_objs_t *info, table_t **group_table,
    table_t **dset_table);

static void init_table(table_t **tbl);

