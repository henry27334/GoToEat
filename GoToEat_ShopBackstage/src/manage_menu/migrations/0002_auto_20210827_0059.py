# Generated by Django 2.2.10 on 2021-08-26 16:59

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('manage_menu', '0001_initial'),
    ]

    operations = [
        migrations.DeleteModel(
            name='ProductAddrule',
        ),
        migrations.AlterModelOptions(
            name='productsize',
            options={},
        ),
        migrations.AlterModelOptions(
            name='sort',
            options={},
        ),
        migrations.RenameField(
            model_name='shopdetail',
            old_name='shop_username',
            new_name='shop_chinese',
        ),
        migrations.RemoveField(
            model_name='tableunit',
            name='ID',
        ),
        migrations.RemoveField(
            model_name='tableunit',
            name='Unit',
        ),
        migrations.AddField(
            model_name='product',
            name='hide',
            field=models.IntegerField(default=''),
        ),
        migrations.AddField(
            model_name='tableunit',
            name='id',
            field=models.AutoField(db_column='ID', default='', primary_key=True, serialize=False),
        ),
        migrations.AddField(
            model_name='tableunit',
            name='unit',
            field=models.TextField(db_column='Unit', default=''),
        ),
        migrations.AlterField(
            model_name='advertise',
            name='id',
            field=models.AutoField(primary_key=True, serialize=False),
        ),
    ]
