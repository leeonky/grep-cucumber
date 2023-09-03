package com.github.leeonky.dal.extensions;

import com.github.leeonky.dal.DAL;
import com.github.leeonky.dal.extensions.basic.file.util.FileGroup;
import com.github.leeonky.dal.runtime.Extension;

import static com.github.leeonky.dal.extensions.basic.binary.BinaryExtension.readAll;
import static com.github.leeonky.dal.extensions.basic.string.Methods.string;

@SuppressWarnings("unused")
public class DalExtension implements Extension {

    @Override
    public void extend(DAL dal) {
        FileGroup.register("feature", inputStream -> string(readAll(inputStream)));
    }
}
