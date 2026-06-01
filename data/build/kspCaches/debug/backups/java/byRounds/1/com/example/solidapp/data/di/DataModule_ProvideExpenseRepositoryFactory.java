package com.example.solidapp.data.di;

import com.example.solidapp.data.local.dao.ExpenseDao;
import com.example.solidapp.domain.repository.ExpenseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DataModule_ProvideExpenseRepositoryFactory implements Factory<ExpenseRepository> {
  private final Provider<ExpenseDao> daoProvider;

  public DataModule_ProvideExpenseRepositoryFactory(Provider<ExpenseDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ExpenseRepository get() {
    return provideExpenseRepository(daoProvider.get());
  }

  public static DataModule_ProvideExpenseRepositoryFactory create(
      Provider<ExpenseDao> daoProvider) {
    return new DataModule_ProvideExpenseRepositoryFactory(daoProvider);
  }

  public static ExpenseRepository provideExpenseRepository(ExpenseDao dao) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideExpenseRepository(dao));
  }
}
